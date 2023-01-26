package com.soyvictorherrera.bdates.core.date

import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

class DateProvider @Inject constructor(
    private val formatters: DateFormattersContract
) : DateProviderContract {
    override val currentLocalDate: LocalDate
        get() = LocalDate.of(2023, 6, 1)

    override val currentLocalDateTime: LocalDateTime
        get() = LocalDateTime.now()

    override fun formatDateAsDay(date: LocalDate): String {
        return formatters.dayFormatter.format(date)
    }
}