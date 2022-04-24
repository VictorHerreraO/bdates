package com.soyvictorherrera.bdates.modules.eventList.framework.presentation

import androidx.lifecycle.ViewModel
import com.soyvictorherrera.bdates.core.arch.UseCase
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.GetEventListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class EventListViewModel @Inject constructor(
    private val getEventListUseCase: UseCase<Unit, Flow<List<Event>>>
) : ViewModel()
