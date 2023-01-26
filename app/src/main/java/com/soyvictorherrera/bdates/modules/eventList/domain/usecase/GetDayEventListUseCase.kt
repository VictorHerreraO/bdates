package com.soyvictorherrera.bdates.modules.eventList.domain.usecase

import com.soyvictorherrera.bdates.core.arch.UseCase
import com.soyvictorherrera.bdates.core.date.DateProviderContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Get the list of events happening today
 */
interface GetDayEventListUseCaseContract : UseCase<Unit, List<Event>> {
    suspend fun execute(): List<Event>

    override suspend fun execute(params: Unit): List<Event> = execute()
}

class GetDayEventListUseCase @Inject constructor(
    dateProvider: DateProviderContract,
    private val getEventListUseCase: GetEventListUseCaseContract
) : GetDayEventListUseCaseContract {
    private val today = dateProvider.currentLocalDate

    override suspend fun execute(): List<Event> {
        return getEventListUseCase.execute()
            .first()
            .filter { event ->
                event.currentYearOccurrence == today
            }
    }
}