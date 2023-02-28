package com.soyvictorherrera.bdates.modules.eventList.framework.presentation

import java.time.LocalDate

data class AddEventViewState(
    val eventName: String,
    val selectedDate: LocalDate,
    val editMode: EditMode,
    val isYearDisabled: Boolean,
)

enum class EditMode {
    CREATE, EDIT
}