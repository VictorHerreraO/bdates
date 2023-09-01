package com.soyvictorherrera.bdates.modules.eventList.data.datasource.remote

import com.soyvictorherrera.bdates.core.network.ListResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EventApi {
    @GET("circles/{circleId}/events")
    suspend fun getCircleEvents(
        @Path("circleId") circleId: String,
        @Query("sinceTimestamp") sinceTimestamp: Long?,
    ): ListResponseDto<EventResponseDto>
}
