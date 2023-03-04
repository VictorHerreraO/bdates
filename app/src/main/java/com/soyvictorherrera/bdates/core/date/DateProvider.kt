package com.soyvictorherrera.bdates.core.date

import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

class DateProvider @Inject constructor(
    private val clock: Clock,
    private val formatters: DateFormattersContract,
) : DateProviderContract {
    override val currentLocalDate: LocalDate
        get() = LocalDate.now(clock)

    override val currentLocalDateTime: LocalDateTime
        get() = LocalDateTime.now(clock)

    override fun formatDateAsDay(date: LocalDate): String {
        return formatters.dayFormatter.format(date)
    }
}