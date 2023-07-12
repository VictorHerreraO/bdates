package com.soyvictorherrera.bdates.modules.eventList.data.datasource.local

import com.soyvictorherrera.bdates.core.persistence.randomUUID
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.EventDataSourceContract
import com.soyvictorherrera.bdates.modules.eventList.data.mapper.EventEntityToModelMapperContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import javax.inject.Inject

interface LocalEventDataSourceContract : EventDataSourceContract<Event> {
    suspend fun getEvent(eventId: String): Event?
    suspend fun createEvent(event: Event): String
    suspend fun updateEvent(event: Event)
    suspend fun deleteEvent(eventId: String)
}

class LocalEventDataSource @Inject constructor(
    private val dao: EventDao,
    private val mapper: EventEntityToModelMapperContract,
) : LocalEventDataSourceContract {

    override suspend fun getEventList(): List<Event> {
        return dao.getAll().map(mapper::map)
    }

    override suspend fun getEvent(eventId: String): Event? {
        return dao.getById(eventId)?.let(mapper::map)
    }

    override suspend fun createEvent(event: Event): String {
        return event
            .copy(id = randomUUID())
            .let(mapper::reverseMap)
            .also {
                dao.upsertAll(it)
            }.id
    }

    override suspend fun updateEvent(event: Event) {
        if (event.id.isNullOrEmpty()) {
            return
        }
        dao.upsertAll(event.let(mapper::reverseMap))
    }

    override suspend fun deleteEvent(eventId: String) {
        dao.deleteById(eventId)
    }
}
