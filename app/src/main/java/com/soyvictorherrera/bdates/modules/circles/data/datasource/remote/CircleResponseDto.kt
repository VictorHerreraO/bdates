package com.soyvictorherrera.bdates.modules.circles.data.datasource.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CircleResponseDto(
    @field:Json(name = "id") val id: String?,
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "owner") val owner: String?,
    @field:Json(name = "admins") val admins: CircleAdmins?,
    @field:Json(name = "created_date") val createdDate: Long?,
    @field:Json(name = "updated_date") val updatedDate: Long?,
    @field:Json(name = "tier") val tier: Int?,
    @field:Json(name = "event_count") val eventCount: Int?,
)

typealias CircleAdmins = Map<String, Boolean>
