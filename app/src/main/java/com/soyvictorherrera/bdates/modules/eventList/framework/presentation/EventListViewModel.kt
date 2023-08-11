package com.soyvictorherrera.bdates.modules.eventList.framework.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soyvictorherrera.bdates.core.arch.execute
import com.soyvictorherrera.bdates.core.date.DateProviderContract
import com.soyvictorherrera.bdates.core.event.ConsumableEvent
import com.soyvictorherrera.bdates.core.event.NavigationEvent
import com.soyvictorherrera.bdates.core.network.onError
import com.soyvictorherrera.bdates.core.network.onSuccess
import com.soyvictorherrera.bdates.core.resource.ResourceManagerContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.FilterEventListArgs
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.FilterEventListUseCaseContract
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.GetDayEventListUseCaseContract
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.GetNonDayEventListUseCaseContract
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.UpdateEventsUseCaseContract
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.properties.Delegates

@HiltViewModel
class EventListViewModel @Inject constructor(
    dateProvider: DateProviderContract,
    private val resourceManager: ResourceManagerContract,
    private val getDayEventList: GetDayEventListUseCaseContract,
    private val getNonDayEventList: GetNonDayEventListUseCaseContract,
    private val filterEventListUseCase: FilterEventListUseCaseContract,
    private val updateEventList: UpdateEventsUseCaseContract,
) : ViewModel() {

    //region View Props
    private val _navigation = MutableLiveData<NavigationEvent>()
    val navigation: LiveData<NavigationEvent>
        get() = _navigation

    private val _events = MutableLiveData<List<EventViewState>>()
    val events: LiveData<List<EventViewState>>
        get() = _events

    private val _todayEvents = MutableLiveData<List<TodayEventViewState>>()
    val todayEvents: LiveData<List<TodayEventViewState>>
        get() = _todayEvents

    private val _isRefreshing = MutableLiveData(false)
    val isRefreshing: LiveData<Boolean>
        get() = _isRefreshing

    private val _errorMessage = MutableLiveData<ConsumableEvent<Error>?>(null)
    val errorMessage: LiveData<ConsumableEvent<Error>?>
        get() = _errorMessage
    //endregion

    private val today: LocalDate = dateProvider.currentLocalDate
    private val longFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("EEEE, dd/MM")

    private var allEvents by Delegates.observable(emptyList<Event>()) { _, _, list ->
        processEventList(list)
    }
    private var dayEvents by Delegates.observable(emptyList<Event>()) { _, _, list ->
        processDayEventList(list)
    }
    private var query = ""

    init {
        getData()
        refreshData()
    }

    private fun getData(): Unit = with(viewModelScope) {
        launch {
            dayEvents = getDayEventList.execute()
        }
        launch {
            allEvents = getNonDayEventList.execute()
        }
    }

    private fun refreshData() {
        viewModelScope.launch {
            _isRefreshing.value = true

            updateEventList.execute()
                .onSuccess {
                    Timber.d("Success")
                    getData()
                }
                .onError { _, cause ->
                    Timber.d(cause, "Show refresh error")
                    _errorMessage.value = ConsumableEvent(Error.UnableToRefresh)
                }

            _isRefreshing.value = false
        }
    }

    fun onQueryTextChanged(query: String) {
        this.query = query
        processEventList(allEvents)
    }

    fun onEventClick(eventId: String) {
        _navigation.value = NavigationEvent.PreviewEventBottomSheet(eventId = eventId)
    }

    fun onAddEventClick() {
        _navigation.value = NavigationEvent.AddEventBottomSheet()
    }

    private fun processEventList(events: List<Event>) = viewModelScope.launch {
        filterEventListUseCase.execute(
            FilterEventListArgs(
                eventList = events,
                query = query
            )
        ).fold(
            onSuccess = { filtered ->
                filtered.sortedBy { event ->
                    // Sort by upcoming
                    event.nextOccurrence
                }.map { event ->
                    // Map to View State
                    val nextOccurrence = event.nextOccurrence!!
                    val remainingTime = ChronoUnit.DAYS
                        .between(today, nextOccurrence)
                    EventViewState(
                        id = event.id!!,
                        remainingTimeValue = remainingTime.toString(),
                        remainingTimeUnit = remainingTime.let {
                            if (it == 1L) resourceManager.getString("time_unit_day")
                            else resourceManager.getString("time_unit_days")
                        },
                        name = event.name,
                        description = nextOccurrence.let { date ->
                            val formatted = date.format(longFormatter)
                            return@let event.year?.let { birthYear ->
                                val yearsOld = nextOccurrence.year.minus(birthYear)
                                "$formatted " + resourceManager.getString(
                                    identifier = "event_birthday_description",
                                    yearsOld
                                )
                            } ?: formatted
                        }
                    )
                }
            },
            onFailure = {
                Timber.e(it)
                emptyList()
            }
        ).let {
            _events.value = it
        }
    }

    private fun processDayEventList(events: List<Event>) {
        events.map { event ->
            TodayEventViewState(
                id = event.id!!,
                friendAge = event.year?.let { birthYear ->
                    today.year.minus(birthYear).toString()
                },
                friendName = event.name,
                eventType = resourceManager.getString("event_birthday_title")
            )
        }.let {
            _todayEvents.value = it
        }
    }

    fun refresh() = refreshData()

}

sealed class Error {
    object UnableToRefresh : Error()
}
