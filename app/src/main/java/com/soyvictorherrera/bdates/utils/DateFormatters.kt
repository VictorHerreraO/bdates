package com.soyvictorherrera.bdates.utils

import java.time.format.DateTimeFormatter

object DateFormatters {
    const val DATE_PATTERN = "dd/MM/yyyy";
    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN)
    const val DAY_PATTERN = "dd MMM"
    val dayFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(DAY_PATTERN)
}
