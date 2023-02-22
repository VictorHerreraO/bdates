package com.soyvictorherrera.bdates.modules.circles.data.datasource.local

import com.soyvictorherrera.bdates.modules.circles.data.datasource.CircleDataSourceContract
import javax.inject.Inject

interface LocalCircleDataSourceContract : CircleDataSourceContract<CircleEntity>

class LocalCircleDataSource @Inject constructor(

) : LocalCircleDataSourceContract {
    override suspend fun getCircles(): List<CircleEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun getCircle(id: String): CircleEntity {
        TODO("Not yet implemented")
    }

    override suspend fun createCircle(circle: CircleEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun updateCircle(circle: CircleEntity) {
        TODO("Not yet implemented")
    }
}
