package com.soyvictorherrera.bdates.modules.eventList.data.datasource

import kotlinx.coroutines.flow.Flow

interface EventDataSourceContract<T> {
    fun getEventList(): Flow<List<T>>
}
