package com.soyvictorherrera.bdates.modules.eventList.framework.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soyvictorherrera.bdates.core.date.DateProviderContract
import com.soyvictorherrera.bdates.core.navigation.NavigationEvent
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

    private val _navigation = MutableStateFlow<NavigationEvent?>(null)
    val navigation: StateFlow<NavigationEvent?> = _navigation.asStateFlow()

    private val _events = MutableStateFlow<List<EventViewState>>(emptyList())
    val events: StateFlow<List<EventViewState>> = _events.asStateFlow()

    private val _todayEvents = MutableStateFlow<List<TodayEventViewState>>(emptyList())
    val todayEvents: StateFlow<List<TodayEventViewState>> = _todayEvents.asStateFlow()

    private val _requestPermissionSignal = MutableStateFlow(true)
    val requestPermissionSignal: StateFlow<Boolean> = _requestPermissionSignal.asStateFlow()

    private val _showMissingPermissionMessage = MutableStateFlow(false)
    val showMissingPermissionMessage: StateFlow<Boolean> = _showMissingPermissionMessage.asStateFlow()

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
    }

    private fun getData() {
        viewModelScope.launch {
            this@EventListViewModel.dayEvents = getDayEventList.execute()
            this@EventListViewModel.allEvents = getNonDayEventList.execute()
        }
    }

    fun onQueryTextChanged(query: String) {
        this.query = query
        processEventList(allEvents)
    }

    fun onEventClick(eventId: String) {
        _navigation.value = NavigationEvent.EventBottomSheet(eventId = eventId)
    }

    fun onAddEventClick() {
        _navigation.value = NavigationEvent.EventBottomSheet()
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
                emptyList()
            }
        ).let { result ->
            _events.update { result }
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
        }.let { result ->
            _todayEvents.update { result }
        }
    }

    fun refresh() = getData()
}
