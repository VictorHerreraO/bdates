package com.soyvictorherrera.bdates.modules.circles.data.repository

import com.soyvictorherrera.bdates.core.network.Resource
import com.soyvictorherrera.bdates.core.network.asError
import com.soyvictorherrera.bdates.core.network.asSuccess
import com.soyvictorherrera.bdates.modules.circles.data.datasource.local.LocalCircleDataSourceContract
import com.soyvictorherrera.bdates.modules.circles.data.datasource.remote.RemoteCircleDataSourceContract
import com.soyvictorherrera.bdates.modules.circles.domain.model.Circle
import javax.inject.Inject

class CircleRepository @Inject constructor(
    private val localDataSource: LocalCircleDataSourceContract,
    private val remoteDataSource: RemoteCircleDataSourceContract,
) : CircleRepositoryContract {

    override suspend fun getCircles(): Resource<List<Circle>> {
        return remoteDataSource.runCatching {
            getCircles()
        }.fold(
            onSuccess = { circles ->
                circles.forEach { circle ->
                    localDataSource.updateCircle(circle)
                }
                localDataSource.getCircles().asSuccess()
            },
            onFailure = {
                localDataSource.getCircles().asError(cause = it)
            }
        )
    }

    override suspend fun getCircle(id: String): Circle {
        return localDataSource
            .getCircle(id)
    }

    override suspend fun createCircle(circle: Circle): String {
        if (!circle.id.isNullOrEmpty()) {
            throw IllegalArgumentException("Can't create a circle with a provided ID")
        }
        return localDataSource.createCircle(circle)
    }

    override suspend fun updateCircle(circle: Circle) {
        if (circle.id.isNullOrEmpty()) {
            throw IllegalArgumentException("Circle ID is missing")
        }
        localDataSource.updateCircle(circle)
    }
}
