package com.soyvictorherrera.bdates.modules.eventList.data.repository

import com.soyvictorherrera.bdates.core.persistence.OnCreated
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event

interface EventRepositoryContract {
    suspend fun getEventList(): List<Event>

    suspend fun getEvent(eventId: String): Event

    suspend fun createEvent(event: Event, onCreated: OnCreated? = null)

    suspend fun deleteEvent(eventId: String)
}