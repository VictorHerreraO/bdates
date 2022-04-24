package com.soyvictorherrera.bdates.modules.eventList.data.datasource

import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import kotlinx.coroutines.flow.Flow

class EventDatasource : EventDataSourceContract {
    override fun getEventList(): Flow<List<Event>> {
        TODO("Not yet implemented")
    }
}
