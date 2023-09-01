package com.soyvictorherrera.bdates.modules.eventList.framework.presentation

import com.soyvictorherrera.bdates.core.event.ConsumableEvent
import java.time.LocalDate

data class AddEventViewState(
    val eventName: String,
    val selectedDate: LocalDate,
    val selectedYear: Int?,
    val editMode: EditMode,
    val isYearDisabled: Boolean,
    val validYearRange: IntRange,
    val isLoading: Boolean,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val isError: Boolean = false,
    val error: ConsumableEvent<AddEventError>? = null,
) {
    val isSaveEnabled: Boolean
        get() {
            val isYearSelectionValid = (isYearDisabled || selectedYear in validYearRange)
            return isSuccess &&
                    eventName.isNotBlank() &&
                    isYearSelectionValid &&
                    !isSaving &&
                    !isDeleting
        }

    val isDeleteEnabled: Boolean
        get() = isSuccess && !isSaving && !isDeleting

    val isSuccess: Boolean
        get() = !isLoading && !isError
}

enum class EditMode {
    CREATE, EDIT
}

enum class AddEventError {
    ERROR_LOADING,
    ERROR_SAVING_EVENT,
    ERROR_DELETING_EVENT,
}
