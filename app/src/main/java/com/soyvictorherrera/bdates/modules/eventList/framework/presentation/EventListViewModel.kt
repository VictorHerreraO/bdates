package com.soyvictorherrera.bdates.modules.eventList.framework.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soyvictorherrera.bdates.core.arch.UseCase
import com.soyvictorherrera.bdates.core.resource.ResourceManagerContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.FilterEventListArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class EventListViewModel @Inject constructor(
    private val resourceManager: ResourceManagerContract,
    private val getEventListUseCase: UseCase<Unit, Flow<List<Event>>>,
    private val filterEventListUseCase: UseCase<FilterEventListArgs, Result<List<Event>>>
) : ViewModel() {

    private val _events = MutableLiveData<List<EventViewState>>()
    val events: LiveData<List<EventViewState>>
        get() = _events

    private val _todayEvents = MutableLiveData<List<TodayEventViewState>>()
    val todayEvents: LiveData<List<TodayEventViewState>>
        get() = _todayEvents

    private val today: LocalDate = LocalDate.now()
    private val longFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("EEEE, dd/MM")

    private var allEvents by Delegates.observable(emptyList<Event>()) { _, _, list ->
        processEventList(list)
    }
    private var dayEvents by Delegates.observable(emptyList<Event>()) { _, _, list ->
        processDayEventList(list)
    }
    private var query = ""

    init {
        viewModelScope.launch {
            getEventListUseCase(Unit).collect { events ->
                val (dayEvents, allEvents) = events.map { event ->
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
                }.partition { event ->
                    event.currentYearOccurrence == today
                }
                this@EventListViewModel.dayEvents = dayEvents
                this@EventListViewModel.allEvents = allEvents
            }
        }
    }

    fun onQueryTextChanged(query: String) {
        this.query = query
        processEventList(allEvents)
    }

    private fun processEventList(events: List<Event>) = viewModelScope.launch {
        filterEventListUseCase(
            FilterEventListArgs(
                eventList = events,
                query = query
            )
        ).fold(
            onSuccess = { filtered ->
                filtered.sortedBy { event ->
                    // Sort by upcoming
                    event.nextOccurrence
                }.map { event ->
                    // Map to View State
                    val nextOccurrence = event.nextOccurrence!!
                    val remainingTime = ChronoUnit.DAYS
                        .between(today, nextOccurrence)
                    EventViewState(
                        id = event.id,
                        remainingTimeValue = remainingTime.toString(),
                        remainingTimeUnit = remainingTime.let {
                            if (it == 1L) resourceManager.getString("time_unit_day")
                            else resourceManager.getString("time_unit_days")
                        },
                        name = event.name,
                        description = nextOccurrence.let { date ->
                            val formatted = date.format(longFormatter)
                            return@let event.year?.let { birthYear ->
                                val yearsOld = nextOccurrence.year.minus(birthYear)
                                "$formatted " + resourceManager.getString(
                                    identifier = "event_birthday_description",
                                    yearsOld
                                )
                            } ?: formatted
                        }
                    )
                }
            },
            onFailure = {
                Timber.e(it)
                emptyList()
            }
        ).let {
            _events.value = it
        }
    }

    private fun processDayEventList(events: List<Event>) {
        events.map { event ->
            TodayEventViewState(
                id = event.id,
                friendAge = event.year?.let { birthYear ->
                    today.year.minus(birthYear).toString()
                },
                friendName = event.name,
                eventType = resourceManager.getString("event_birthday_title")
            )
        }.let {
            _todayEvents.value = it
        }
    }

}
