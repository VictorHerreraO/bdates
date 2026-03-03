package com.soyvictorherrera.bdates.modules.eventList.framework.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soyvictorherrera.bdates.core.arch.execute
import com.soyvictorherrera.bdates.core.date.DateProviderContract
import com.soyvictorherrera.bdates.core.event.NavigationEvent
import com.soyvictorherrera.bdates.core.network.onError
import com.soyvictorherrera.bdates.core.network.onSuccess
import com.soyvictorherrera.bdates.core.resource.ResourceManagerContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import com.soyvictorherrera.bdates.modules.eventList.domain.model.nextOccurrenceAge
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.FilterEventListArgs
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.FilterEventListUseCaseContract
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.GetDayEventListUseCaseContract
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.GetNonDayEventListUseCaseContract
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.UpdateEventsUseCaseContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class EventListViewModel @Inject constructor(
    private val dateProvider: DateProviderContract,
    private val resourceManager: ResourceManagerContract,
    private val getDayEventList: GetDayEventListUseCaseContract,
    private val getNonDayEventList: GetNonDayEventListUseCaseContract,
    private val filterEventListUseCase: FilterEventListUseCaseContract,
    private val updateEventList: UpdateEventsUseCaseContract,
) : ViewModel() {

    private val _navigation = MutableStateFlow<NavigationEvent?>(null)
    val navigation: StateFlow<NavigationEvent?> = _navigation.asStateFlow()

    private val _uiState = MutableStateFlow(EventListState())
    val uiState: StateFlow<EventListState> = _uiState.asStateFlow()

    private val today: LocalDate = dateProvider.currentLocalDate

    private var allEvents by Delegates.observable(emptyList<Event>()) { _, _, list ->
        processEventList(list)
    }
    private var dayEvents by Delegates.observable(emptyList<Event>()) { _, _, list ->
        processDayEventList(list)
    }

    init {
        getData()
        refreshData()
    }

    fun onAction(action: EventListAction) {
        when (action) {
            is EventListAction.Refresh -> getData()
            is EventListAction.ChangeQuery -> {
                _uiState.update { it.copy(query = action.query) }
                processEventList(allEvents)
            }
            is EventListAction.EventClick -> {
                _navigation.value = NavigationEvent.PreviewEventBottomSheet(eventId = action.eventId)
            }
            is EventListAction.AddEventClick -> {
                _navigation.value = NavigationEvent.AddEventBottomSheet()
            }
            is EventListAction.NotificationPermissionStateCheck -> {
                _uiState.update { it.copy(showMissingPermissionMessage = !action.isGranted) }
            }
            is EventListAction.NotificationPermissionStateChanged -> {
                _uiState.update {
                    it.copy(
                        requestPermission = false,
                        showMissingPermissionMessage = !action.isGranted,
                    )
                }
            }
            is EventListAction.OpenAppSettings -> Unit // Handled by the Fragment/Screen
        }
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
            _uiState.update { it.copy(isRefreshing = true) }

            updateEventList.execute()
                .onSuccess {
                    Timber.d("Success")
                    getData()
                }
                .onError { _, cause ->
                    Timber.d(cause, "Show refresh error")
                    _uiState.update { it.copy(errorMessage = resourceManager.getString("event_list_error_fetch_list")) }
                }

            _uiState.update { it.copy(isRefreshing = false) }
        }
    }

    private fun processEventList(events: List<Event>) = viewModelScope.launch {
        val currentQuery = _uiState.value.query
        filterEventListUseCase.execute(
            FilterEventListArgs(
                eventList = events,
                query = currentQuery,
            )
        ).fold(
            onSuccess = { filtered ->
                filtered.sortedBy { event ->
                    event.nextOccurrence
                }.map { event ->
                    val nextOccurrence = event.nextOccurrence!!
                    val remainingTime = ChronoUnit.DAYS.between(today, nextOccurrence)
                    EventViewState(
                        id = event.id!!,
                        remainingTimeValue = remainingTime.toString(),
                        remainingTimeUnit = remainingTime.let {
                            if (it == 1L) resourceManager.getString("time_unit_day")
                            else resourceManager.getString("time_unit_days")
                        },
                        name = event.name,
                        description = nextOccurrence.let { date ->
                            val formatted = dateProvider.formatDateAsDayAndMonth(date)
                            return@let event.nextOccurrenceAge?.let { yearsOld ->
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
            _uiState.update { it.copy(events = result) }
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
            _uiState.update { it.copy(todayEvents = result) }
        }
    }
}

sealed class Error {
    object UnableToRefresh : Error()
}
