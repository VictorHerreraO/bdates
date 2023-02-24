package com.soyvictorherrera.bdates.modules.eventList.data.repository

import com.soyvictorherrera.bdates.modules.eventList.data.datasource.assets.AssetEventDatasourceContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class EventRepository @Inject constructor(
    private val assetsDataSource: AssetEventDatasourceContract
) : EventRepositoryContract {
    override fun getEventList(): Flow<List<Event>> {
        return assetsDataSource.getEventList()
    }
}