package com.soyvictorherrera.bdates.modules.eventList.data.repository

import com.soyvictorherrera.bdates.modules.eventList.data.datasource.EventDataSourceContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EventRepository @Inject constructor(
    private val dataSource: EventDataSourceContract
) : EventRepositoryContract {
    override fun getEventList(): Flow<List<Event>> {
        return dataSource.getEventList()
    }
}