package com.soyvictorherrera.bdates.modules.eventList.data.repository

import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepositoryContract{
    fun getEventList(): Flow<List<Event>>
}