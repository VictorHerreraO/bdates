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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class EventListViewModel @Inject constructor(
    private val getEventListUseCase: UseCase<Unit, Flow<List<Event>>>
) : ViewModel() {

    private val _events = MutableLiveData<List<EventViewState>>()
    val events: LiveData<List<EventViewState>>
        get() = _events

    private val today: LocalDate = LocalDate.now()
    private val longFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("EE, dd/MM")

    init {
        viewModelScope.launch {
            getEventListUseCase(Unit).collect { events ->

                _events.value = events.map { event ->
                    // Add event occurrences
                    val currentYearOccurrence = LocalDate.of(
                        today.year,
                        event.monthOfYear,
                        event.dayOfMonth
                    )
                    event.copy(
                        currentYearOccurrence = currentYearOccurrence,
                        nextOccurrence = if (currentYearOccurrence.isAfter(today)) currentYearOccurrence
                        else currentYearOccurrence.plusYears(1L)
                    )
                }.sortedBy { event ->
                    // Sort by upcoming
                    event.nextOccurrence
                }.map { event ->
                    // Map to View State
                    val nextOccurrence = event.nextOccurrence!!
                    EventViewState(
                        id = event.id,
                        remainingTimeValue = ChronoUnit.DAYS
                            .between(today, nextOccurrence)
                            .toString(),
                        remainingTimeUnit = "Días",
                        name = event.name,
                        description = nextOccurrence.let { date ->
                            val formatted = date.format(longFormatter)
                            return@let event.year?.let { birthYear ->
                                val birthDate = LocalDate.of(
                                    birthYear,
                                    date.month,
                                    date.dayOfMonth
                                )
                                "$formatted • Cumple " + ChronoUnit.YEARS
                                    .between(birthDate, nextOccurrence)
                                    .toString()
                            } ?: formatted
                        }
                    )
                }
            }
        }
    }
}
