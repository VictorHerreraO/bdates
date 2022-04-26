package com.soyvictorherrera.bdates.modules.eventList.domain.model

data class Event(
    val id: String,
    val name: String,
    val dayOfMonth: Int,
    val monthOfYear: Int,
    val year: Int?
)
