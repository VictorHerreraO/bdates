package com.soyvictorherrera.bdates.modules.eventList.framework.presentation

import java.time.LocalDate

data class AddEventViewState(
    val eventName: String,
    val selectedDate: LocalDate,
    val selectedYear: Int?,
    val editMode: EditMode,
    val isYearDisabled: Boolean,
    val isSaveEnabled: Boolean,
    val validYearRange: IntRange,
)

enum class EditMode {
    CREATE, EDIT
}