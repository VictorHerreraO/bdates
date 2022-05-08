package com.soyvictorherrera.bdates.modules.eventList.data.datasource

import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventDataSourceContract {
    fun getEventList(): Flow<List<Event>>
}
