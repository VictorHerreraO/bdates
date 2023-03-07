package com.soyvictorherrera.bdates.modules.eventList.framework.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soyvictorherrera.bdates.core.date.DateProviderContract
import com.soyvictorherrera.bdates.core.navigation.NavigationEvent
import com.soyvictorherrera.bdates.modules.circles.data.preferences.CirclePreferencesContract
import com.soyvictorherrera.bdates.modules.eventList.data.repository.EventRepositoryContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val INITIAL_YEAR_RANGE = 1901

@HiltViewModel
class AddEventViewModel @Inject constructor(
    private val dateProvider: DateProviderContract,
    private val eventRepository: EventRepositoryContract,
    private val circlePreferences: CirclePreferencesContract,
) : ViewModel() {

    private val _navigation = MutableStateFlow<NavigationEvent?>(null)
    val navigation: StateFlow<NavigationEvent?> = _navigation

    private val _state = MutableStateFlow(
        AddEventViewState(
            eventName = "",
            selectedDate = dateProvider.currentLocalDate,
            selectedYear = dateProvider.currentLocalDate.year,
            editMode = EditMode.CREATE,
            isYearDisabled = false,
            isLoading = false,
            validYearRange = INITIAL_YEAR_RANGE..dateProvider.currentLocalDate.year
        )
    )
    val state: StateFlow<AddEventViewState> = _state

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
                id = null,
                circleId = localCircleId,
                name = eventName.trim(),
                dayOfMonth = selectedDate.dayOfMonth,
                monthOfYear = selectedDate.monthValue,
                year = selectedDate.year.takeIf { isYearDisabled.not() }
            )
        }

        _state.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            eventRepository.createEvent(event) {
                _navigation.value = NavigationEvent.NavigateBack()
            }
        }
    }

    fun onDeleteClick() {
        TODO()
    }

}
