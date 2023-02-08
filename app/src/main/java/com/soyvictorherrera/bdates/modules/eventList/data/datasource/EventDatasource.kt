package com.soyvictorherrera.bdates.modules.eventList.data.datasource

import com.soyvictorherrera.bdates.core.arch.Mapper
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber

class EventDatasource @Inject constructor(
    private val assets: AssetFileManagerContract,
    private val jsonToEventMapper: Mapper<JSONObject, Event>
) : EventDataSourceContract {
    companion object {
        const val EVENTS_FILE = "events.json"
    }

    override fun getEventList(): Flow<List<Event>> {
        return try {
            val jsonString = assets.openAsString(EVENTS_FILE)
            val array = JSONArray(jsonString)
            val events = mutableListOf<Event>()

            array.forEach { node ->
                if (node is JSONObject) {
                    events.add(jsonToEventMapper.map(node))
                }
            }

            flowOf(events)
        } catch (e: Exception) {
            Timber.w(e)
            flowOf(emptyList())
        }

    }
}
