package com.soyvictorherrera.bdates.modules.eventList.data.datasource

import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import org.json.JSONObject

fun JSONObject.toEvent(): Event {
    return Event(
        id = optString("id"),
        name = optString("nombre"),
        dayOfMonth = optString("fechaNac").split("/")[0].toInt(),
        monthOfYear = optString("fechaNac").split("/")[1].toInt(),
        year = try {
            optString("fechaNac").split("/")[2].toInt()
        } catch (e: IndexOutOfBoundsException) {
            null
        }
    )
}
