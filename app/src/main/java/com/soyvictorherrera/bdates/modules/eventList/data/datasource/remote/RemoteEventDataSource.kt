package com.soyvictorherrera.bdates.modules.eventList.data.datasource.remote

import com.soyvictorherrera.bdates.modules.eventList.data.datasource.EventDataSourceContract
import com.soyvictorherrera.bdates.modules.eventList.data.mapper.EventDtoToModelMapperContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import javax.inject.Inject

interface RemoteEventDataSourceContract : EventDataSourceContract<Event> {
    suspend fun getCircleEventList(
        circleId: String,
        lastUpdateDate: Long?,
    ): List<Event>
}

class RemoteEventDataSource @Inject constructor(
    private val eventApi: EventApi,
    private val mapper: EventDtoToModelMapperContract,
) : RemoteEventDataSourceContract {
    override suspend fun getCircleEventList(
        circleId: String,
        lastUpdateDate: Long?,
    ): List<Event> {
        return eventApi.getCircleEvents(
            circleId = circleId,
            sinceTimestamp = lastUpdateDate
        ).data.mapNotNull { mapper.map(it, circleId) }
    }

    override suspend fun getEventList(): List<Event> {
        TODO("Not yet implemented")
    }
}
