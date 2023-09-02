package com.soyvictorherrera.bdates.modules.circles.data.datasource.local

import com.soyvictorherrera.bdates.core.persistence.randomUUID
import com.soyvictorherrera.bdates.modules.circles.data.datasource.CircleDataSourceContract
import com.soyvictorherrera.bdates.modules.circles.data.mapper.CircleEntityToModelMapperContract
import com.soyvictorherrera.bdates.modules.circles.domain.model.Circle
import javax.inject.Inject

interface LocalCircleDataSourceContract : CircleDataSourceContract<Circle>

class LocalCircleDataSource @Inject constructor(
    private val dao: CircleDao,
    private val mapper: CircleEntityToModelMapperContract,
) : LocalCircleDataSourceContract {
    override suspend fun getCircles(): List<Circle> {
        return dao.getAll().map(mapper::map)
    }

    override suspend fun getCircle(id: String): Circle {
        return dao.getById(id).let(mapper::map)
    }

    override suspend fun createCircle(circle: Circle): String {
        return circle
            .let(mapper::reverseMap)
            .copy(id = randomUUID())
            .also {
                dao.upsertAll(it)
            }.id
    }

    override suspend fun updateCircle(circle: Circle) {
        if (circle.id.isNullOrEmpty()) {
            throw IllegalArgumentException("id must not be empty")
        }
        dao.upsertAll(circle.let(mapper::reverseMap))
    }
}
