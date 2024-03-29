package com.soyvictorherrera.bdates.modules.eventList.data.datasource.assets

import com.soyvictorherrera.bdates.core.arch.Mapper
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.EventDataSourceContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import javax.inject.Inject
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber

interface AssetEventDatasourceContract : EventDataSourceContract<Event>

class AssetEventDatasource @Inject constructor(
    private val assets: AssetFileManagerContract,
    private val jsonToEventMapper: Mapper<JSONObject, Event>,
) : AssetEventDatasourceContract {
    companion object {
        const val EVENTS_FILE = "events.json"
    }

    override suspend fun getEventList(): List<Event> {
        return try {
            val jsonString = assets.openAsString(EVENTS_FILE)
            val array = JSONArray(jsonString)
            val events = mutableListOf<Event>()

            array.forEach { node ->
                if (node is JSONObject) {
                    events.add(jsonToEventMapper.map(node))
                }
            }

            events
        } catch (e: Exception) {
            Timber.w(e)
            emptyList()
        }

    }
}
