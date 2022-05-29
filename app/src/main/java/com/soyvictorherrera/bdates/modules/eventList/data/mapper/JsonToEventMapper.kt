package com.soyvictorherrera.bdates.modules.eventList.data.mapper

import com.soyvictorherrera.bdates.core.arch.Mapper
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import org.json.JSONObject

object JsonToEventMapper : Mapper<JSONObject, Event>() {
    private const val KEY_ID = "id"
    private const val KEY_NAME = "nombre"
    private const val KEY_BIRTH_DATE = "fechaNac"

    override fun map(value: JSONObject): Event = with(value) {
        return Event(
            id = optString(KEY_ID),
            name = optString(KEY_NAME),
            dayOfMonth = optString(KEY_BIRTH_DATE).split("/")[0].toInt(),
            monthOfYear = optString(KEY_BIRTH_DATE).split("/")[1].toInt(),
            year = try {
                optString(KEY_BIRTH_DATE).split("/")[2].toInt()
            } catch (e: IndexOutOfBoundsException) {
                null
            }
        )
    }
}
