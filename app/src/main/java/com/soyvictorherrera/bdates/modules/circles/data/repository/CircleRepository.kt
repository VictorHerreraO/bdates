package com.soyvictorherrera.bdates.modules.circles.data.repository

import com.soyvictorherrera.bdates.modules.circles.data.datasource.local.LocalCircleDataSourceContract
import com.soyvictorherrera.bdates.modules.circles.data.datasource.remote.RemoteCircleDataSourceContract
import com.soyvictorherrera.bdates.modules.circles.domain.model.Circle
import javax.inject.Inject
import timber.log.Timber

class CircleRepository @Inject constructor(
    private val localDataSource: LocalCircleDataSourceContract,
    private val remoteDataSource: RemoteCircleDataSourceContract,
) : CircleRepositoryContract {

    override suspend fun getCircles(): List<Circle> {
        Timber.d("fetch circles")
        return remoteDataSource.runCatching {
            getCircles()
        }.fold(
            onSuccess = { circles ->
                Timber.d("circles fetched")
                circles.forEach {
                    localDataSource.updateCircle(it)
                }
                circles
            },
            onFailure = {
                Timber.e(it)
                localDataSource.getCircles()
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
