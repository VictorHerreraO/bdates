package com.soyvictorherrera.bdates.modules.eventList.domain.usecase

import com.soyvictorherrera.bdates.core.arch.UseCase
import com.soyvictorherrera.bdates.modules.eventList.data.repository.EventRepositoryContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEventListUseCase @Inject constructor(
    private val repository: EventRepositoryContract
) : UseCase<Unit, Flow<List<Event>>>() {
    override fun invoke(params: Unit): Flow<List<Event>> {
        return repository.getEventList()
    }
}