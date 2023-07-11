package com.soyvictorherrera.bdates.modules.circles.data.datasource.remote

import com.soyvictorherrera.bdates.modules.circles.data.datasource.CircleDataSourceContract
import com.soyvictorherrera.bdates.modules.circles.data.mapper.CircleResponseDtoToModelMapperContract
import com.soyvictorherrera.bdates.modules.circles.domain.model.Circle
import javax.inject.Inject

interface RemoteCircleDataSourceContract : CircleDataSourceContract<Circle>

class RemoteCircleDataSource @Inject constructor(
    private val api: CircleApi,
    private val mapper: CircleResponseDtoToModelMapperContract,
) : RemoteCircleDataSourceContract {
    override suspend fun getCircles(): List<Circle> {
        val dto = api.getCircle("-NXpd9eKytV2M3bNT_m3")
        return listOf(mapper.map(dto))
    }

    override suspend fun getCircle(id: String): Circle {
        TODO("Not yet implemented")
    }

    override suspend fun createCircle(circle: Circle): String {
        TODO("Not yet implemented")
    }

    override suspend fun updateCircle(circle: Circle) {
        TODO("Not yet implemented")
    }
}