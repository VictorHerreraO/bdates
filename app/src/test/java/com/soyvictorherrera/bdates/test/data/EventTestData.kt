package com.soyvictorherrera.bdates.test.data

import com.soyvictorherrera.bdates.modules.eventList.data.datasource.local.EventEntity
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import java.time.LocalDate

private val DEFAULT_DATE = LocalDate.of(2016, 3, 14) // PI day

fun event(withDate: LocalDate = DEFAULT_DATE) = Event(
    id = "id-event",
    circleId = "id-circle",
    name = "Event name",
    dayOfMonth = withDate.dayOfMonth,
    monthOfYear = withDate.monthValue,
    year = withDate.year,
)

fun eventEntity(date: LocalDate = DEFAULT_DATE) = EventEntity(
    id = "id-event",
    circleId = "id-circle",
    name = "Event name",
    dayOfMonth = date.dayOfMonth,
    monthOfYear = date.monthValue,
    year = date.year,
)

fun eventModelFoo() = event().copy(
    id = "foo-event",
    name = "Foo event",
)

fun eventModelBar() = event().copy(
    id = "bar-event",
    name = "Bar event"
)


fun eventEntityBar() = eventEntity().copy(
    id = "bar-event",
    name = "Bar event"
)
