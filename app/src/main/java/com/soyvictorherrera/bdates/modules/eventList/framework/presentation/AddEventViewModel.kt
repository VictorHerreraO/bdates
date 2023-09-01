package com.soyvictorherrera.bdates.modules.eventList.framework.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soyvictorherrera.bdates.core.date.DateProviderContract
import com.soyvictorherrera.bdates.core.event.NavigationEvent
import com.soyvictorherrera.bdates.core.event.asConsumableEvent
import com.soyvictorherrera.bdates.modules.circles.data.preferences.CirclePreferencesContract
import com.soyvictorherrera.bdates.modules.eventList.data.repository.EventRepositoryContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import com.soyvictorherrera.bdates.modules.eventList.framework.ui.AddEventBottomSheetArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

private const val INITIAL_YEAR_RANGE = 1901

@HiltViewModel
class AddEventViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    private val dateProvider: DateProviderContract,
    private val eventRepository: EventRepositoryContract,
    private val circlePreferences: CirclePreferencesContract,
) : ViewModel() {

    private val _navigation = MutableStateFlow<NavigationEvent?>(null)
    val navigation: StateFlow<NavigationEvent?> = _navigation

    private val eventId = AddEventBottomSheetArgs.fromSavedStateHandle(stateHandle).eventId
    private var currentEvent: Event? = null

    private val _state = MutableStateFlow(
        AddEventViewState(
            eventName = "",
            selectedDate = dateProvider.currentLocalDate,
            selectedYear = dateProvider.currentLocalDate.year,
            editMode = if (eventId.isNullOrEmpty()) {
                EditMode.CREATE
            } else {
                EditMode.EDIT
            },
            isYearDisabled = false,
            isLoading = true,
            isSaving = false,
            validYearRange = INITIAL_YEAR_RANGE..dateProvider.currentLocalDate.year,
        )
    )
    val state: StateFlow<AddEventViewState> = _state.asStateFlow()

    init {
        if (eventId != null) {
            loadEvent(eventId)
        } else {
            _state.update { it.copy(isLoading = false) }
        }
    }

    fun onEventNameChange(eventName: String) {
        _state.update {
            it.copy(
                eventName = eventName,
            )
        }
    }

    fun onDateSelected(selectedDate: LocalDate) {
        _state.update {
            it.copy(
                selectedDate = selectedDate,
                selectedYear = selectedDate.year,
            )
        }
    }

    fun onYearSelected(selectedYear: Int) {
        onDateSelected(
            _state.value.selectedDate.withYear(selectedYear)
        )
    }

    fun onYearCleared() {
        _state.update {
            it.copy(
                selectedDate = it.selectedDate.withYear(dateProvider.currentLocalDate.year),
                selectedYear = null,
            )
        }
    }

    fun onYearDisabled(disabled: Boolean) {
        _state.update {
            it.copy(isYearDisabled = disabled)
        }
    }

    fun onActionClick() {
        val localCircleId = circlePreferences.localCircleId ?: return
        val event = _state.value.run {
            Event(
                id = eventId,
                circleId = currentEvent?.circleId ?: localCircleId,
                name = eventName.trim(),
                dayOfMonth = selectedDate.dayOfMonth,
                monthOfYear = selectedDate.monthValue,
                year = selectedDate.year.takeIf { isYearDisabled.not() }
            )
        }

        _state.update {
            it.copy(isSaving = true)
        }

        viewModelScope.launch {
            eventRepository.runCatching {
                if (event.id.isNullOrEmpty()) {
                    createEvent(event)
                } else {
                    updateEvent(event)
                }
            }.onSuccess {
                _navigation.value = NavigationEvent.NavigateBack()
            }.onFailure { throwable ->
                Timber.e(throwable, "Unable to save event")
                _state.update {
                    it.copy(
                        isSaving = false,
                        error = AddEventError.ERROR_SAVING_EVENT.asConsumableEvent()
                    )
                }
            }
        }
    }

    fun onDeleteClick() {
        eventId ?: return

        _state.update {
            it.copy(isDeleting = true)
        }

        viewModelScope.launch {
            eventRepository.runCatching {
                deleteEvent(eventId)
            }.onSuccess {
                _navigation.value = NavigationEvent.NavigateBack()
            }.onFailure { throwable ->
                Timber.e(throwable, "Unable to delete event")
                _state.update {
                    it.copy(
                        isDeleting = false,
                        error = AddEventError.ERROR_DELETING_EVENT.asConsumableEvent()
                    )
                }
            }
        }
    }

    private fun loadEvent(eventId: String) {
        viewModelScope.launch {
            eventRepository.runCatching {
                getEvent(eventId)
            }.onSuccess { event ->
                currentEvent = event
                _state.update {
                    it.copy(
                        eventName = event.name,
                        selectedDate = LocalDate.of(
                            event.year ?: it.selectedDate.year,
                            event.monthOfYear,
                            event.dayOfMonth
                        ),
                        selectedYear = event.year,
                        isYearDisabled = event.year == null,
                        isLoading = false
                    )
                }
            }.onFailure { throwable ->
                Timber.e(throwable, "Unable to load event :(")
                _state.update {
                    it.copy(
                        isLoading = false,
                        isError = true,
                        error = AddEventError.ERROR_LOADING.asConsumableEvent()
                    )
                }
            }
        }
    }

}
