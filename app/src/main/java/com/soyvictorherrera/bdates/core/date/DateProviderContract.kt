package com.soyvictorherrera.bdates.core.date

import java.time.LocalDate
import java.time.LocalDateTime

interface DateProviderContract {
    val currentLocalDate: LocalDate
    val currentLocalDateTime: LocalDateTime

    /**
     * Formats the given [date] into a human readable day name and day of month:
     *
     * Saturday 15
     */
    fun formatDateAsDay(date: LocalDate): String

    /**
     * Formats the given [date] into a human readable day name and day of month:
     *
     * Saturday 03/15
     */
    fun formatDateAsDayAndMonth(date: LocalDate): String
}
