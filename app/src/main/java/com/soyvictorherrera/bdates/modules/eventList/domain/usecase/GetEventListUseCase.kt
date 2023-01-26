package com.soyvictorherrera.bdates.modules.eventList.domain.usecase

import com.soyvictorherrera.bdates.core.arch.UseCase
import com.soyvictorherrera.bdates.modules.date.DateProviderContract
import com.soyvictorherrera.bdates.modules.eventList.data.repository.EventRepositoryContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

private const val ONE_YEAR = 1L

interface GetEventListUseCaseContract : UseCase<Unit, Flow<List<Event>>> {
    suspend fun execute(): Flow<List<Event>>

    override suspend fun execute(params: Unit): Flow<List<Event>> = execute()
}

class GetEventListUseCase @Inject constructor(
    dateProvider: DateProviderContract,
    private val repository: EventRepositoryContract
) : GetEventListUseCaseContract {
    private val today: LocalDate = dateProvider.currentLocalDate

    override suspend fun execute(): Flow<List<Event>> {
        return repository.getEventList().map { events ->
            events.map { event ->
                // Add event occurrences
                val currentYearOccurrence = LocalDate.of(
                    today.year,
                    event.monthOfYear,
                    event.dayOfMonth
                )
                // Update event
                event.copy(
                    currentYearOccurrence = currentYearOccurrence,
                    nextOccurrence = if (currentYearOccurrence.isAfter(today)) {
                        currentYearOccurrence
                    } else {
                        currentYearOccurrence.plusYears(ONE_YEAR)
                    }
                )
            }
        }
    }
}