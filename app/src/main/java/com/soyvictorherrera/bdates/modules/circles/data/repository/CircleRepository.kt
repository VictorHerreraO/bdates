package com.soyvictorherrera.bdates.modules.circles.data.repository

import com.soyvictorherrera.bdates.core.arch.Mapper
import com.soyvictorherrera.bdates.modules.circles.data.datasource.local.CircleEntity
import com.soyvictorherrera.bdates.modules.circles.data.datasource.local.LocalCircleDataSourceContract
import com.soyvictorherrera.bdates.modules.circles.domain.model.Circle
import javax.inject.Inject

class CircleRepository @Inject constructor(
    private val localDataSource: LocalCircleDataSourceContract,
    private val localMapper: Mapper<CircleEntity, Circle>
) : CircleRepositoryContract {

    override suspend fun getCircles(): List<Circle> {
        TODO("Not yet implemented")
    }

    override suspend fun getCircle(id: String): Circle {
        TODO("Not yet implemented")
    }

    override suspend fun createCircle(circle: Circle) {
        TODO("Not yet implemented")
    }

    override suspend fun updateCircle(circle: Circle) {
        TODO("Not yet implemented")
    }
}
