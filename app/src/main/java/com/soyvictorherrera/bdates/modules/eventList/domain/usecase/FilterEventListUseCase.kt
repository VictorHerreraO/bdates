package com.soyvictorherrera.bdates.modules.eventList.domain.usecase

import com.soyvictorherrera.bdates.core.arch.UseCase
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class FilterEventListArgs(
    val eventList: List<Event>,
    val query: String
)

class FilterEventListUseCase : UseCase<FilterEventListArgs, Flow<List<Event>>>() {
    override fun invoke(params: FilterEventListArgs): Flow<List<Event>> = flow {
        emit(params.eventList.filter { event ->
            event.name.contains(
                other = params.query,
                ignoreCase = true
            )
        })
    }
}
