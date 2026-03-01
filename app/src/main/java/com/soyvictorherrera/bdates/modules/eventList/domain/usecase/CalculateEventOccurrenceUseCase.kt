package com.soyvictorherrera.bdates.modules.eventList.domain.usecase

import com.soyvictorherrera.bdates.core.arch.UseCase
import com.soyvictorherrera.bdates.core.date.DateProviderContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import java.time.LocalDate
import javax.inject.Inject

private const val ONE_YEAR = 1L

interface CalculateEventOccurrenceUseCaseContract : UseCase<Event, Event>

class CalculateEventOccurrenceUseCase @Inject constructor(
    private val dateProvider: DateProviderContract,
) : CalculateEventOccurrenceUseCaseContract {
    private val today: LocalDate
        get() = dateProvider.currentLocalDate

    override suspend fun execute(params: Event): Event {
        val currentYearOccurrence = LocalDate.of(
            today.year,
            params.monthOfYear,
            params.dayOfMonth
        )
        // Update event
        return params.copy(
            currentYearOccurrence = currentYearOccurrence,
            nextOccurrence = if (currentYearOccurrence.isAfter(today)) {
                currentYearOccurrence
            } else {
                currentYearOccurrence.plusYears(ONE_YEAR)
            }
        )
    }
}
