package com.soyvictorherrera.bdates.modules.eventList.domain.model

import java.time.LocalDateTime

data class Event(
    val id: String,
    val name: String,
    val date: LocalDateTime
)
