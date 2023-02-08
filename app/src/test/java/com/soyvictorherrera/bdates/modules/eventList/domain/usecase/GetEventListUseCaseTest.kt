package com.soyvictorherrera.bdates.modules.eventList.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.soyvictorherrera.bdates.core.date.DateProviderContract
import com.soyvictorherrera.bdates.modules.eventList.data.repository.EventRepositoryContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever
import java.time.LocalDate

@RunWith(MockitoJUnitRunner::class)
class GetEventListUseCaseTest {

    @Mock
    private lateinit var mockDateProvider: DateProviderContract

    @Mock
    private lateinit var mockEvents: EventRepositoryContract

    private val today = LocalDate.of(2023, 7, 1)

    private lateinit var useCase: GetEventListUseCase

    @Before
    fun setup() {
        whenever(mockDateProvider.currentLocalDate).thenReturn(today)

        useCase = GetEventListUseCase(
            dateProvider = mockDateProvider,
            repository = mockEvents
        )
    }

    @Test
    fun get_event_list_calculates_current_year_occurrence(): Unit = runBlocking {
        val eventDate = today.plusMonths(1)
        val event = Event(
            id = "event-id",
            name = "event name",
            dayOfMonth = eventDate.dayOfMonth,
            monthOfYear = eventDate.monthValue,
            year = null
        )
        val expectedEvent = event.copy(
            currentYearOccurrence = eventDate,
            nextOccurrence = eventDate
        )
        val expectedList = listOf(event)
        val expectedFlow = flowOf(expectedList)
        whenever(mockEvents.getEventList()).thenReturn(expectedFlow)

        val result = useCase.execute().first()

        assertThat(result).isNotNull()
        assertThat(result).isNotEmpty()
        assertThat(result).hasSize(expectedList.size)
        assertThat(result.first()).isEqualTo(expectedEvent)
    }

    @Test
    fun get_event_list_calculates_next_year_occurrence(): Unit = runBlocking {
        // Make the event happen on the past so next occurrence should be  a year later
        val eventDate = today.minusMonths(1)
        val event = Event(
            id = "event-id",
            name = "event name",
            dayOfMonth = eventDate.dayOfMonth,
            monthOfYear = eventDate.monthValue,
            year = null
        )
        val expectedEvent = event.copy(
            currentYearOccurrence = eventDate,
            nextOccurrence = eventDate.plusYears(1)
        )
        val expectedList = listOf(event)
        val expectedFlow = flowOf(expectedList)
        whenever(mockEvents.getEventList()).thenReturn(expectedFlow)

        val result = useCase.execute().first()

        assertThat(result).isNotNull()
        assertThat(result).isNotEmpty()
        assertThat(result).hasSize(expectedList.size)
        assertThat(result.first()).isEqualTo(expectedEvent)
    }

    @Test(expected = RuntimeException::class)
    fun get_event_list_error_propagates(): Unit = runBlocking {
        val expectedFlow = flow<List<Event>> { throw RuntimeException() }
        whenever(mockEvents.getEventList()).thenReturn(expectedFlow)

        useCase.execute().first()
    }
}
