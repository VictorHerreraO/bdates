package com.soyvictorherrera.bdates.modules.eventList.domain.usecase

import com.soyvictorherrera.bdates.core.arch.UseCase
import com.soyvictorherrera.bdates.core.date.DateProviderContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import javax.inject.Inject

/**
 * Get the list of events which do not happen today
 */
interface GetNonDayEventListUseCaseContract : UseCase<Unit, List<Event>> {
    suspend fun execute(): List<Event>

    override suspend fun execute(params: Unit): List<Event> = execute()
}

class GetNonDayEventListUseCase @Inject constructor(
    dateProvider: DateProviderContract,
    private val getEventList: GetEventListUseCaseContract,
) : GetNonDayEventListUseCaseContract {
    private val today = dateProvider.currentLocalDate

    override suspend fun execute(): List<Event> {
        return getEventList.execute()
            .filter { event ->
                event.currentYearOccurrence != today
            }
    }
}