package com.soyvictorherrera.bdates.modules.eventList.data.repository

import com.soyvictorherrera.bdates.core.coroutines.IODispatcher
import com.soyvictorherrera.bdates.core.persistence.addSource
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.assets.AssetEventDatasourceContract
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.local.LocalEventDataSourceContract
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.remote.RemoteEventDataSourceContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

class EventRepository @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val assetsDataSource: AssetEventDatasourceContract,
    private val localDataSource: LocalEventDataSourceContract,
    private val remoteDataSource: RemoteEventDataSourceContract,
) : EventRepositoryContract {
    override suspend fun getEventList(): List<Event> {
        return mutableListOf<Event>()
            // .addSource(assetsDataSource) { getEventList() }
            .addSource(localDataSource) {
                getEventList().also {
                    Timber.d("Fetched local event source")
                }
            }
    }

    override suspend fun getCircleEventList(
        circleId: String,
        lastUpdateDate: Long?,
    ): List<Event> {
        remoteDataSource.getCircleEventList(
            circleId = circleId,
            lastUpdateDate = lastUpdateDate
        ).forEach {
            if (it.deleted) {
                Timber.d("deleting event ${it.id}")
                localDataSource.deleteEvent(it.id.orEmpty())
            } else {
                Timber.d("updating event ${it.name}")
                localDataSource.updateEvent(it)
            }
        }
        return emptyList()
    }

    override suspend fun getEvent(eventId: String): Event = withContext(ioDispatcher) {
        return@withContext localDataSource
            .getEvent(eventId)
            ?: throw IllegalArgumentException("no event with id $eventId")
    }

    override suspend fun createEvent(event: Event): String {
        if (!event.id.isNullOrEmpty()) {
            throw IllegalArgumentException("Can't create an event with a provided ID")
        }
        return localDataSource.createEvent(event)
    }

    override suspend fun updateEvent(event: Event) {
        if (event.id.isNullOrEmpty()) {
            throw IllegalArgumentException("Can't update an event  without an ID")
        }
        localDataSource.updateEvent(event)
    }

    override suspend fun deleteEvent(eventId: String) = withContext(ioDispatcher) {
        return@withContext localDataSource.deleteEvent(eventId)
    }
}