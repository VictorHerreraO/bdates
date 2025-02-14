package com.soyvictorherrera.bdates.modules.eventList.framework.presentation

import java.time.LocalDate

data class AddEventViewState(
    val eventName: String,
    val selectedDate: LocalDate,
    val selectedYear: Int?,
    val editMode: EditMode,
    val isYearDisabled: Boolean,
    val validYearRange: IntRange,
    val isLoading: Boolean,
) {
    val isSaveEnabled: Boolean
        get() {
            val isYearSelectionValid = (isYearDisabled || selectedYear in validYearRange)
            return eventName.isNotBlank() &&
                    isYearSelectionValid &&
                    !isLoading
        }
}

enum class EditMode {
    CREATE, EDIT
}