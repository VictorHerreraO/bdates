package com.soyvictorherrera.bdates.core.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ListResponseDto<T>(
    @field:Json(name = "data") val data: List<T>,
)
