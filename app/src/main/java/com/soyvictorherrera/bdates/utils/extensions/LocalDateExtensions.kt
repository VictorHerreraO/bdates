package com.soyvictorherrera.bdates.utils.extensions

import com.soyvictorherrera.bdates.utils.DateFormatters
import java.time.LocalDate


fun LocalDate.toDayString(): String {
    return this.format(DateFormatters.dayFormatter)
}
