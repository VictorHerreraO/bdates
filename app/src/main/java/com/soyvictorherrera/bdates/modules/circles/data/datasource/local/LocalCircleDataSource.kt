package com.soyvictorherrera.bdates.modules.circles.data.datasource.local

import com.soyvictorherrera.bdates.modules.circles.data.datasource.CircleDataSourceContract
import java.util.UUID
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

    override suspend fun createCircle(circle: CircleEntity) {
        circle
            .copy(id = randomUUID())
            .let {
                dao.upsertAll(it)
            }
    }

    override suspend fun updateCircle(circle: CircleEntity) {
        if (circle.id.isEmpty()) {
            throw IllegalArgumentException("id must not be empty")
        }
        dao.upsertAll(circle)
    }

    private fun randomUUID(): String = UUID
        .randomUUID()
        .toString()
}
