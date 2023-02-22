package com.soyvictorherrera.bdates.modules.circles.data.mapper

import com.soyvictorherrera.bdates.core.arch.Mapper
import com.soyvictorherrera.bdates.modules.circles.data.datasource.local.CircleEntity
import com.soyvictorherrera.bdates.modules.circles.domain.model.Circle

object CircleEntityToModelMapper : Mapper<CircleEntity, Circle>() {
    override fun map(value: CircleEntity): Circle = with(value) {
        return Circle(
            id = id,
            name = name,
            description = description,
        )
    }

    override fun reverseMap(value: Circle): CircleEntity = with(value) {
        return CircleEntity(
            id = id,
            name = name,
            description = description,
        )
    }
}
