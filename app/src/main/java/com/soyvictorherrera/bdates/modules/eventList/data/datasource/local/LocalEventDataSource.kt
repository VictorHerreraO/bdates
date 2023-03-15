package com.soyvictorherrera.bdates.modules.eventList.data.datasource.local

import com.soyvictorherrera.bdates.core.persistence.randomUUID
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.EventDataSourceContract
import javax.inject.Inject

interface LocalEventDataSourceContract : EventDataSourceContract<EventEntity> {
    suspend fun getEvent(eventId: String): EventEntity?
    suspend fun createEvent(event: EventEntity): String
    suspend fun deleteEvent(eventId: String)
}

class LocalEventDataSource @Inject constructor(
    private val dao: EventDao,
) : LocalEventDataSourceContract {

    override suspend fun getEventList(): List<EventEntity> {
        return dao.getAll()
    }

    override suspend fun getEvent(eventId: String): EventEntity? {
        return dao.getById(eventId)
    }

    override suspend fun createEvent(event: EventEntity): String {
        return event
            .copy(id = randomUUID())
            .also {
                dao.upsertAll(it)
            }.id
    }

    override suspend fun deleteEvent(eventId: String) {
        dao.deleteById(eventId)
    }
}
