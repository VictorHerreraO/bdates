package com.soyvictorherrera.bdates.modules.eventList.data.datasource

import android.util.Log
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.json.JSONArray
import org.json.JSONObject

class EventDatasource constructor(
    private val assets: AssetFileManager
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
                    events.add(node.toEvent())
                }
            }

            flowOf(events)
        } catch (e: Exception) {
            Log.w(this::class.simpleName, e)
            flowOf(emptyList())
        }

    }
}
