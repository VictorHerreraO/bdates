package com.soyvictorherrera.bdates.modules.eventList.data.mapper

import com.soyvictorherrera.bdates.core.arch.Mapper
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.remote.EventResponseDto
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event

interface EventDtoToModelMapperContract : Mapper<EventResponseDto, Event?> {
    fun map(value: EventResponseDto, circleId: String): Event?
}

object EventDtoToModelMapper : EventDtoToModelMapperContract {
    override fun map(value: EventResponseDto): Event? = throw UnsupportedOperationException(
        "Use map (value, circleId) instead"
    )

    override fun map(value: EventResponseDto, circleId: String): Event? = with(value) {
        return if (deleted == true) {
            Event(
                id = id,
                circleId = circleId,
                name = "",
                dayOfMonth = -1,
                monthOfYear = -1,
                year = null,
                deleted = true,
            )
        } else {
            Event(
                id = id,
                circleId = circleId,
                name = name ?: return null,
                dayOfMonth = day_of_month ?: return null,
                monthOfYear = month_of_year ?: return null,
                year = year,
            )
        }
    }
}
