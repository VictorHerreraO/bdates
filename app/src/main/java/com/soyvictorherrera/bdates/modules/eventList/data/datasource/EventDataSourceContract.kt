package com.soyvictorherrera.bdates.modules.eventList.data.datasource

import kotlinx.coroutines.flow.Flow

interface EventDataSourceContract<T> {
    suspend fun getEventList(): Flow<List<T>>

    suspend fun createEvent(event: T): String
}
