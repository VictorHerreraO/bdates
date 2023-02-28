package com.soyvictorherrera.bdates.modules.eventList.framework.presentation

import androidx.lifecycle.ViewModel
import com.soyvictorherrera.bdates.core.date.DateProviderContract
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class AddEventViewModel @Inject constructor(
    dateProvider: DateProviderContract,
) : ViewModel() {

    private val _state = MutableStateFlow(
        AddEventViewState(
            eventName = "",
            selectedDate = dateProvider.currentLocalDate,
            editMode = EditMode.CREATE,
            isYearDisabled = false,
        )
    )
    val state: StateFlow<AddEventViewState> = _state


    fun onEventNameChange(eventName: String) {
        _state.update {
            it.copy(eventName = eventName)
        }
    }

    fun onDateSelected(selectedDate: LocalDate) {
        _state.update {
            it.copy(selectedDate = selectedDate)
        }
    }

    fun onYearDisabled(disabled: Boolean) {
        _state.update {
            it.copy(isYearDisabled = disabled)
        }
    }

    fun onActionClick() {
    }

}
