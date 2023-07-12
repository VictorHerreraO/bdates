package com.soyvictorherrera.bdates.modules.eventList.data.mapper

import com.soyvictorherrera.bdates.core.arch.Mapper
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.local.EventEntity
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event

interface EventEntityToModelMapperContract : Mapper<EventEntity, Event>

object EventEntityToModelMapper : EventEntityToModelMapperContract {
    override fun map(value: EventEntity): Event = with(value) {
        return Event(
            id = id,
            circleId = circleId,
            name = name,
            dayOfMonth = dayOfMonth,
            monthOfYear = monthOfYear,
            year = year
        )
    }

    override fun reverseMap(value: Event): EventEntity = with(value) {
        return EventEntity(
            id = id ?: "",
            circleId = circleId,
            name = name,
            dayOfMonth = dayOfMonth,
            monthOfYear = monthOfYear,
            year = year
        )
    }
}
