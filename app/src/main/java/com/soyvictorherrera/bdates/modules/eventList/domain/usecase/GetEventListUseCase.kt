package com.soyvictorherrera.bdates.modules.eventList.domain.usecase

import com.soyvictorherrera.bdates.core.arch.UseCase
import com.soyvictorherrera.bdates.modules.eventList.data.repository.EventRepositoryContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import javax.inject.Inject

interface GetEventListUseCaseContract : UseCase<Unit, List<Event>>

class GetEventListUseCase @Inject constructor(
    private val repository: EventRepositoryContract,
    private val calculateEventOccurrences: CalculateEventOccurrenceUseCaseContract,
) : GetEventListUseCaseContract {
    override suspend fun execute(params: Unit): List<Event> {
        return repository.getEventList().map { event ->
            // Add event occurrences
            return@map calculateEventOccurrences.execute(event)
        }
    }
}
