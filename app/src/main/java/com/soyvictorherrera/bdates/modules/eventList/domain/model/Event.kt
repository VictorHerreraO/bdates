package com.soyvictorherrera.bdates.modules.eventList.domain.model

import java.time.LocalDate

data class Event(
    val id: String,
    val name: String,
    val dayOfMonth: Int,
    val monthOfYear: Int,
    val year: Int?,
    val currentYearOccurrence: LocalDate? = null,
    val nextOccurrence: LocalDate? = null
)
