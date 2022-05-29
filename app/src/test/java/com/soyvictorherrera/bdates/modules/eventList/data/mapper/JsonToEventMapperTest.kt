package com.soyvictorherrera.bdates.modules.eventList.data.mapper

import com.google.common.truth.Truth.assertThat
import org.json.JSONObject
import org.junit.Test

class JsonToEventMapperTest {

    private val mapper = JsonToEventMapper

    @Test
    fun map_json_to_event() {
        val expectedId = "id-event"
        val expectedName = "event-name"
        val dayOfMonth = 31
        val monthOfYear = 12
        val year = 1969
        val eventDate = "$dayOfMonth/$monthOfYear/$year"
        val json = getInputJson(
            expectedId,
            expectedName,
            eventDate
        )

        val result = mapper.map(json)

        assertThat(result).isNotNull()
        assertThat(result.id).isEqualTo(expectedId)
        assertThat(result.name).isEqualTo(expectedName)
        assertThat(result.dayOfMonth).isEqualTo(dayOfMonth)
        assertThat(result.monthOfYear).isEqualTo(monthOfYear)
        assertThat(result.year).isEqualTo(year)
    }

    @Test
    fun map_json_to_event_with_no_year() {
        val expectedId = "id-event"
        val expectedName = "event-name"
        val dayOfMonth = 31
        val monthOfYear = 12
        val eventDate = "$dayOfMonth/$monthOfYear"
        val json = getInputJson(
            eventId = expectedId,
            eventName = expectedName,
            eventDate = eventDate
        )

        val result = mapper.map(json)

        assertThat(result).isNotNull()
        assertThat(result.id).isEqualTo(expectedId)
        assertThat(result.name).isEqualTo(expectedName)
        assertThat(result.dayOfMonth).isEqualTo(dayOfMonth)
        assertThat(result.monthOfYear).isEqualTo(monthOfYear)
        assertThat(result.year).isNull()
    }

    @Test(expected = NumberFormatException::class)
    fun map_json_to_event_with_illegal_date_should_fail() {
        val expectedId = "id-event"
        val expectedName = "event-name"
        val dayOfMonth = "1o"
        val monthOfYear = 12
        val year = 1969
        val eventDate = "$dayOfMonth/$monthOfYear/$year"
        val json = getInputJson(
            eventId = expectedId,
            eventName = expectedName,
            eventDate = eventDate
        )

        mapper.map(json)
    }

    private fun getInputJson(
        eventId: String,
        eventName: String,
        eventDate: String
    ) = JSONObject()
        .put("id", eventId)
        .put("nombre", eventName)
        .put("fechaNac", eventDate)

}
