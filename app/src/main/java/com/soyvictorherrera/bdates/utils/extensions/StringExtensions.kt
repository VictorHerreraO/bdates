package com.soyvictorherrera.bdates.utils.extensions

import com.soyvictorherrera.bdates.utils.DateFormatters
import java.time.LocalDate
import java.time.format.DateTimeParseException

/**
 * Convierte un string con formato `dd/MM/yyyy` a un [LocalDate]
 */
@Throws(DateTimeParseException::class)
fun String.toLocalDate(): LocalDate {
    return LocalDate.parse(this, DateFormatters.dateFormatter)
}
