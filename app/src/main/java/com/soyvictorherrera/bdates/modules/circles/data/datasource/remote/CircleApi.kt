package com.soyvictorherrera.bdates.modules.circles.data.datasource.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface CircleApi {
    @GET("circles/{id}")
    suspend fun getCircle(
        @Path("id") id: String,
    ): CircleResponseDto
}