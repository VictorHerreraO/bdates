package com.soyvictorherrera.bdates.modules.date

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
}