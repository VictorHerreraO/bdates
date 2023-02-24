package com.soyvictorherrera.bdates.modules.circles.data.datasource.local

import com.soyvictorherrera.bdates.core.persistence.randomUUID
import com.soyvictorherrera.bdates.modules.circles.data.datasource.CircleDataSourceContract
import javax.inject.Inject

interface LocalCircleDataSourceContract : CircleDataSourceContract<CircleEntity>

class LocalCircleDataSource @Inject constructor(
    private val dao: CircleDao
) : LocalCircleDataSourceContract {
    override suspend fun getCircles(): List<CircleEntity> {
        return dao.getAll()
    }

    override suspend fun getCircle(id: String): CircleEntity {
        return dao.getById(id)
    }

    override suspend fun createCircle(circle: CircleEntity): String {
        return circle
            .copy(id = randomUUID())
            .also {
                dao.upsertAll(it)
            }.id
    }

    override suspend fun updateCircle(circle: CircleEntity) {
        if (circle.id.isEmpty()) {
            throw IllegalArgumentException("id must not be empty")
        }
        dao.upsertAll(circle)
    }
}
