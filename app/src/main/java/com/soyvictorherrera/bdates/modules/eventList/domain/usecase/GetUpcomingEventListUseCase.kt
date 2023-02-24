package com.soyvictorherrera.bdates.modules.eventList.domain.usecase

import com.soyvictorherrera.bdates.core.arch.UseCase
import com.soyvictorherrera.bdates.core.date.DateProviderContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import javax.inject.Inject

const val ONE_WEEK = 1L

interface GetUpcomingEventListUseCaseContract : UseCase<Unit, List<Event>> {
    suspend fun execute(): List<Event>

    override suspend fun execute(params: Unit): List<Event> = execute()
}

class GetUpcomingEventListUseCase @Inject constructor(
    private val dateProvider: DateProviderContract,
    private val getEventListUse: GetEventListUseCaseContract
) : GetUpcomingEventListUseCaseContract {
    override suspend fun execute(): List<Event> {
        val today = dateProvider.currentLocalDate
        val oneWeekLater = today.plusWeeks(ONE_WEEK)

        return getEventListUse.execute()
            .filter {
                it.nextOccurrence == oneWeekLater
            }
    }
}
