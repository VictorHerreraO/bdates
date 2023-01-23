package com.soyvictorherrera.bdates.modules.date

import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

class DateProvider @Inject constructor(

) : DateProviderContract {
    override val currentLocalDate: LocalDate
        get() = LocalDate.now()
    override val currentLocalDateTime: LocalDateTime
        get() = LocalDateTime.now()
}