package com.soyvictorherrera.bdates.core.date

import com.soyvictorherrera.bdates.core.resource.ResourceManagerContract
import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

private const val DATE_FORMAT_DAY_AND_MONTH = "date_format_long_day_with_month"

class DateProvider @Inject constructor(
    private val clock: Clock,
    private val formatters: DateFormattersContract,
    private val resourceManager: ResourceManagerContract,
) : DateProviderContract {
    override val currentLocalDate: LocalDate
        get() = LocalDate.now(clock)

    override val currentLocalDateTime: LocalDateTime
        get() = LocalDateTime.now(clock)

    override fun formatDateAsDay(date: LocalDate): String {
        return formatters.dayFormatter.format(date)
    }

    override fun formatDateAsDayAndMonth(date: LocalDate): String {
        val format = resourceManager.getString(DATE_FORMAT_DAY_AND_MONTH)
        return date.formatWithPattern(format)
    }
}
