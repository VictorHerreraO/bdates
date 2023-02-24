package com.soyvictorherrera.bdates.modules.eventList.data.repository

import com.soyvictorherrera.bdates.core.persistence.OnCreated
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepositoryContract{
    suspend fun getEventList(): Flow<List<Event>>

    suspend fun createEvent(event: Event, onCreated: OnCreated? = null)
}