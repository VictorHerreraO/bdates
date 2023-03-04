package com.soyvictorherrera.bdates.modules.eventList.data.repository

import com.soyvictorherrera.bdates.core.arch.Mapper
import com.soyvictorherrera.bdates.core.persistence.OnCreated
import com.soyvictorherrera.bdates.core.persistence.addSource
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.assets.AssetEventDatasourceContract
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.local.EventEntity
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.local.LocalEventDataSourceContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import javax.inject.Inject
import timber.log.Timber

class EventRepository @Inject constructor(
    private val assetsDataSource: AssetEventDatasourceContract,
    private val localDataSource: LocalEventDataSourceContract,
    private val localMapper: Mapper<EventEntity, Event>,
) : EventRepositoryContract {
    override suspend fun getEventList(): List<Event> {
        return mutableListOf<Event>()
            .addSource(assetsDataSource) { getEventList() }
            .addSource(localDataSource) {
                getEventList().map(localMapper::map).also {
                    Timber.d("Fetched local event source")
                }
            }
    }

    override suspend fun createEvent(event: Event, onCreated: OnCreated?) {
        if (!event.id.isNullOrEmpty()) {
            throw IllegalArgumentException("Can't create an event with a provided ID")
        }
        localMapper
            .reverseMap(event)
            .let {
                localDataSource.createEvent(it)
            }
            .let { id ->
                onCreated?.invoke(id)
            }
    }
}