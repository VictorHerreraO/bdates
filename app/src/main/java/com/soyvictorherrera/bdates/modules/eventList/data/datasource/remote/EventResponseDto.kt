package com.soyvictorherrera.bdates.modules.eventList.data.datasource.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EventResponseDto(
    @field:Json(name = "id") val id: String?,
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "day_of_month") val day_of_month: Int?,
    @field:Json(name = "month_of_year") val month_of_year: Int?,
    @field:Json(name = "year") val year: Int?,
    @field:Json(name = "deleted") val deleted: Boolean?,
)
