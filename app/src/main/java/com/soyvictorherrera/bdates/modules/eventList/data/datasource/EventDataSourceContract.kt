package com.soyvictorherrera.bdates.modules.eventList.data.datasource

interface EventDataSourceContract<T> {
    suspend fun getEventList(): List<T>

    suspend fun createEvent(event: T): String
}
