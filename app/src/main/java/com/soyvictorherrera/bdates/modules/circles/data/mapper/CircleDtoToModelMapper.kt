package com.soyvictorherrera.bdates.modules.circles.data.mapper

import com.soyvictorherrera.bdates.core.arch.Mapper
import com.soyvictorherrera.bdates.modules.circles.data.datasource.remote.CircleResponseDto
import com.soyvictorherrera.bdates.modules.circles.domain.model.Circle

interface CircleDtoToModelMapperContract : Mapper<CircleResponseDto, Circle>

object CircleDtoToModelMapper : CircleDtoToModelMapperContract {
    override fun map(value: CircleResponseDto): Circle = with(value) {
        return Circle(
            id = id,
            name = name.orEmpty(),
            description = "",
            isLocalOnly = false
        )
    }
}
