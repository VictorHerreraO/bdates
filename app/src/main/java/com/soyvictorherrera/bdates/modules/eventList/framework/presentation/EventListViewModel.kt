package com.soyvictorherrera.bdates.modules.eventList.framework.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soyvictorherrera.bdates.core.arch.UseCase
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventListViewModel @Inject constructor(
    private val getEventListUseCase: UseCase<Unit, Flow<List<Event>>>
) : ViewModel() {

    private val _events = MutableLiveData<List<EventViewState>>()
    val events: LiveData<List<EventViewState>>
        get() = _events

    init {
        viewModelScope.launch {
            getEventListUseCase(Unit).collect { events ->
                _events.value = events.sortedBy { event ->
                    event.monthOfYear
                }.map { event ->
                    EventViewState(
                        id = event.id,
                        remainingTimeValue = "0",
                        remainingTimeUnit = "Días",
                        name = event.name,
                        description = "${event.dayOfMonth} / ${event.monthOfYear}"
                    )
                }
            }
        }
    }
}
