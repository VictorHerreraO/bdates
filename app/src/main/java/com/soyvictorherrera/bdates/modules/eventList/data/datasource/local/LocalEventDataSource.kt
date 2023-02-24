package com.soyvictorherrera.bdates.modules.eventList.data.datasource.local

import com.soyvictorherrera.bdates.core.persistence.randomUUID
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.EventDataSourceContract
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface LocalEventDataSourceContract : EventDataSourceContract<EventEntity>

class LocalEventDataSource @Inject constructor(
    private val dao: EventDao
) : LocalEventDataSourceContract {

    override suspend fun getEventList(): Flow<List<EventEntity>> {
        return flowOf(dao.getAll())
    }

    override suspend fun createEvent(event: EventEntity): String {
        return event
            .copy(id = randomUUID())
            .also {
                dao.upsertAll(it)
            }.id
    }
}
