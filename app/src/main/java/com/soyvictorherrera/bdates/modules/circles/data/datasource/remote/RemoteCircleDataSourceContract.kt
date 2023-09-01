package com.soyvictorherrera.bdates.modules.circles.data.datasource.remote

import com.soyvictorherrera.bdates.modules.circles.data.datasource.CircleDataSourceContract
import com.soyvictorherrera.bdates.modules.circles.data.mapper.CircleResponseDtoToModelMapperContract
import com.soyvictorherrera.bdates.modules.circles.data.preferences.CirclePreferencesContract
import com.soyvictorherrera.bdates.modules.circles.domain.model.Circle
import javax.inject.Inject

interface RemoteCircleDataSourceContract : CircleDataSourceContract<Circle>

class RemoteCircleDataSource @Inject constructor(
    private val api: CircleApi,
    private val mapper: CircleResponseDtoToModelMapperContract,
    private val circlePreferences: CirclePreferencesContract,
) : RemoteCircleDataSourceContract {
    override suspend fun getCircles(): List<Circle> {
        return circlePreferences.defaultRemoteCircleId?.let { id ->
            listOf(getCircle(id))
        } ?: emptyList()
    }

    override suspend fun getCircle(id: String): Circle {
        return api.getCircle(id).let(mapper::map)
    }

    override suspend fun createCircle(circle: Circle): String {
        TODO("Not yet implemented")
    }

    override suspend fun updateCircle(circle: Circle) {
        TODO("Not yet implemented")
    }
}