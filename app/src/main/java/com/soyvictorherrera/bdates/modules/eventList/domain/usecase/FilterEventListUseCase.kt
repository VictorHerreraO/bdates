package com.soyvictorherrera.bdates.modules.eventList.domain.usecase

import com.soyvictorherrera.bdates.core.arch.UseCase
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event

data class FilterEventListArgs(
    val eventList: List<Event>,
    val query: String
)

class FilterEventListUseCase : UseCase<FilterEventListArgs, Result<List<Event>>>() {
    override fun invoke(params: FilterEventListArgs): Result<List<Event>> = runCatching {
        params.eventList.filter { event ->
            event.name.contains(
                other = params.query,
                ignoreCase = true
            )
        }
    }
}
