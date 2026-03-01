package com.soyvictorherrera.bdates.modules.eventList.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.soyvictorherrera.bdates.core.arch.execute
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.CalculateEventOccurrenceUseCaseContract
import com.soyvictorherrera.bdates.modules.eventList.data.repository.EventRepositoryContract
import com.soyvictorherrera.bdates.test.data.event
import java.time.LocalDate
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class GetEventListUseCaseTest {

    @Mock
    private lateinit var mockCalculateEventOccurrences: CalculateEventOccurrenceUseCaseContract

    @Mock
    private lateinit var mockEvents: EventRepositoryContract

    private lateinit var useCase: GetEventListUseCase

    @Before
    fun setup() {
        useCase = GetEventListUseCase(
            repository = mockEvents,
            calculateEventOccurrences = mockCalculateEventOccurrences
        )
    }

    @Test
    fun get_event_list_calculates_occurrences(): Unit = runBlocking {
        val event = event()
        val expectedEvent = event.copy(
            currentYearOccurrence = LocalDate.now(), // dummy
            nextOccurrence = LocalDate.now()
        )
        val expectedList = listOf(event)
        whenever(mockEvents.getEventList()).thenReturn(expectedList)
        whenever(mockCalculateEventOccurrences.execute(event)).thenReturn(expectedEvent)

        val result = useCase.execute()

        assertThat(result).isNotNull()
        assertThat(result).isNotEmpty()
        assertThat(result).hasSize(expectedList.size)
        assertThat(result.first()).isEqualTo(expectedEvent)
    }

    @Test(expected = RuntimeException::class)
    fun get_event_list_error_propagates(): Unit = runBlocking {
        whenever(mockEvents.getEventList()).thenThrow(RuntimeException::class.java)

        useCase.execute()
    }
}
