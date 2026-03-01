package com.soyvictorherrera.bdates.core.date

import com.soyvictorherrera.bdates.R
import com.soyvictorherrera.bdates.core.resource.ResourceManagerContract
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

interface DateFormattersContract {
    val dayFormatter: DateTimeFormatter
    val ddMMYYYYFormatter: DateTimeFormatter
}

class DateFormatters @Inject constructor(
    resourceManager: ResourceManagerContract,
) : DateFormattersContract {

    override val dayFormatter: DateTimeFormatter = resourceManager.run {
        DateTimeFormatter.ofPattern(getString(R.string.date_format_day))
    }

    override val ddMMYYYYFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
}

fun LocalDate.formatWithPattern(stringPattern: String): String = DateTimeFormatter
    .ofPattern(stringPattern)
    .format(this)
