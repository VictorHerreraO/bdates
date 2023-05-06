package com.soyvictorherrera.bdates.core.date

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.chrono.ChronoLocalDate

/**
 * Converts `this` [LocalDateTime] to an epoch milli at the given [zoneId]
 */
fun LocalDateTime.toEpochMilli(zoneId: ZoneId = ZoneId.systemDefault()) = this
    .atZone(zoneId)
    .toInstant()
    .toEpochMilli()

/**
 * @see [LocalDate.isEqual]
 * @see [LocalDate.isAfter]
 */
fun LocalDate.isEqualOrAfter(other: ChronoLocalDate): Boolean = (isEqual(other) || isAfter(other))
