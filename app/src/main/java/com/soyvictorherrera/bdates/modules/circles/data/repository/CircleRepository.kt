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
        return localDataSource
            .getCircles()
            .map(localMapper::map)
    }

    override suspend fun getCircle(id: String): Circle {
        return localDataSource
            .getCircle(id)
            .let(localMapper::map)
    }

    override suspend fun createCircle(circle: Circle) {
        if (!circle.id.isNullOrEmpty()) {
            throw IllegalArgumentException("Can't create a circle with a provided ID")
        }
        localMapper
            .reverseMap(circle)
            .let {
                localDataSource.createCircle(it)
            }
    }

    override suspend fun updateCircle(circle: Circle) {
        if (circle.id.isNullOrEmpty()) {
            throw IllegalArgumentException("Circle ID is missing")
        }
        localMapper
            .reverseMap(circle)
            .let {
                localDataSource.updateCircle(it)
            }
    }
}
