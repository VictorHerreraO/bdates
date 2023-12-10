package com.soyvictorherrera.bdates.modules.eventList.framework.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soyvictorherrera.bdates.core.date.DateProviderContract
import com.soyvictorherrera.bdates.core.resource.ResourceManagerContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.FilterEventListArgs
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.FilterEventListUseCaseContract
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.GetDayEventListUseCaseContract
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.GetNonDayEventListUseCaseContract
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@HiltViewModel
class EventListViewModel @Inject constructor(
    dateProvider: DateProviderContract,
    private val resourceManager: ResourceManagerContract,
    private val getDayEventList: GetDayEventListUseCaseContract,
    private val getNonDayEventList: GetNonDayEventListUseCaseContract,
    private val filterEventListUseCase: FilterEventListUseCaseContract,
) : ViewModel() {

    private val _events = MutableLiveData<List<EventViewState>>()
    val events: LiveData<List<EventViewState>>
        get() = _events

    private val _todayEvents = MutableLiveData<List<TodayEventViewState>>()
    val todayEvents: LiveData<List<TodayEventViewState>>
        get() = _todayEvents

    private val _requestPermissionSignal = MutableLiveData(true)
    val requestPermissionSignal: LiveData<Boolean>
        get() = _requestPermissionSignal

    private val _showMissingPermissionMessage = MutableLiveData(false)
    val showMissingPermissionMessage: LiveData<Boolean>
        get() = _showMissingPermissionMessage

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
        viewModelScope.launch {
            this@EventListViewModel.dayEvents = getDayEventList.execute()
            this@EventListViewModel.allEvents = getNonDayEventList.execute()
        }
    }

    fun onQueryTextChanged(query: String) {
        this.query = query
        processEventList(allEvents)
    }

    fun onNotificationPermissionStateCheck(isGranted: Boolean) {
        val requiresPermission = !isGranted
        _showMissingPermissionMessage.value = requiresPermission
    }

    fun onNotificationPermissionStateChanged(isGranted: Boolean) {
        _requestPermissionSignal.value = false
        _showMissingPermissionMessage.value = !isGranted
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
                        id = event.id,
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
                emptyList()
            }
        ).let {
            _events.value = it
        }
    }

    private fun processDayEventList(events: List<Event>) {
        events.map { event ->
            TodayEventViewState(
                id = event.id,
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

}
