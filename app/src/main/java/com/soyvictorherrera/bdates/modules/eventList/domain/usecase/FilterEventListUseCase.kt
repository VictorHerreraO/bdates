package com.soyvictorherrera.bdates.modules.eventList.domain.usecase

import com.soyvictorherrera.bdates.core.arch.UseCase
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import javax.inject.Inject

data class FilterEventListArgs(
    val eventList: List<Event>,
    val query: String
)

interface FilterEventListUseCaseContract : UseCase<FilterEventListArgs, Result<List<Event>>>

class FilterEventListUseCase @Inject constructor(

) : FilterEventListUseCaseContract {
    override suspend fun execute(params: FilterEventListArgs): Result<List<Event>> = runCatching {
        params.eventList.filter { event ->
            event.name.contains(
                other = params.query,
                ignoreCase = true
            )
        }
    }
}
