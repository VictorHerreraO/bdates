package com.soyvictorherrera.bdates.modules.eventList.domain.usecase

import com.soyvictorherrera.bdates.core.arch.UseCase
import com.soyvictorherrera.bdates.modules.eventList.data.repository.EventRepositoryContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import javax.inject.Inject

interface GetEventUseCaseContract : UseCase<String, Result<Event>>

class GetEventUseCase @Inject constructor(
    private val eventRepository: EventRepositoryContract,
    private val calculateEventOccurrence: CalculateEventOccurrenceUseCaseContract
) : GetEventUseCaseContract {
    override suspend fun execute(params: String): Result<Event> {
        return eventRepository.runCatching {
            getEvent(params)
        }.map { event ->
            return@map calculateEventOccurrence.execute(event)
        }
    }
}
