package com.soyvictorherrera.bdates.domain.model

import com.soyvictorherrera.bdates.utils.TODAY
import com.soyvictorherrera.bdates.utils.extensions.toLocalDate
import java.io.Serializable
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class FriendModel(
    val id: String,
    val name: String,
    val birthDateString: String
) : Serializable {

    companion object {
        const val YEARS_UNKNOWN = -1
    }

    /**
     * [FriendModel.birthDateString] parseado como [LocalDate]
     */
    val birthDate: LocalDate? by lazy {
        try {
            birthDateString.toLocalDate()
        } catch (ex: Exception) {
            null
        }
    }

    /**
     * En caso de que la fecha de nacimiento este en formato `dd/MM`
     * se genera una fecha de nacimiento con el año de hoy.
     *
     * Usar esta fecha para calculos y estimaciones
     */
    val stubBirthDate: LocalDate? by lazy {
        val date = if (birthDateString.length <= 5) {
            "$birthDateString/${TODAY.year}"
        } else birthDateString
        try {
            date.toLocalDate()
        } catch (ex: Exception) {
            null
        }
    }

    /**
     * Cuantos años tiene [FriendModel] al día de hoy
     */
    val yearsOld: Int by lazy {
        birthDate?.let {
            ChronoUnit.YEARS.between(birthDate, TODAY).toInt()
        } ?: YEARS_UNKNOWN
    }

    /**
     * Cuantos años cumplirá [FriendModel] en su siguiente cumpleaños
     */
    val nextYearsOld: Int by lazy {
        if (yearsOld == YEARS_UNKNOWN) YEARS_UNKNOWN
        else yearsOld.inc()
    }

    /**
     * Fecha del siguiente cumpleaños
     *
     * `null` en caso de que [FriendModel.birthDate] y [FriendModel.stubBirthDate] sean nulos
     */
    val nextBirthDate: LocalDate? by lazy {
        birthDate?.plusYears(nextYearsOld.toLong())
            ?: stubBirthDate?.let {
                // Calcular la fecha del siguiente cumpleaños según el día del año
                val todayDay = TODAY.dayOfYear
                if (it.dayOfYear >= todayDay) {
                    // Si el día del año es después de hoy, cumple este año
                    TODAY.withDayOfYear(it.dayOfYear)
                } else {
                    // Si el día del año es antes que hoy, cumple al siguiente año
                    it.plusYears(1L)
                }
            }
    }

    /**
     * Días restantes hasta el siguiente cumpleaños
     */
    val daysUntilBirthDate: Int by lazy {
        nextBirthDate?.let {
            ChronoUnit.DAYS.between(TODAY, it).toInt()
        } ?: Int.MAX_VALUE
    }

}
