package com.soyvictorherrera.bdates.modules.date

import java.time.LocalDate
import java.time.LocalDateTime

interface DateProviderContract {
    val currentLocalDate: LocalDate
    val currentLocalDateTime: LocalDateTime
}