package com.soyvictorherrera.bdates.modules.eventList.domain.usecase

import com.soyvictorherrera.bdates.modules.date.DateProviderContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class GetNonDayEventListUseCaseTest {
    @Mock
    private lateinit var mockDateProvider: DateProviderContract

    @Mock
    private lateinit var mockGetEventListUseCase: GetEventListUseCaseContract

    private lateinit var subjectUnderTest: GetNonDayEventListUseCase

    private val today = LocalDate.of(2023, 7, 1)

    @Before
    fun setUp() {
        whenever(mockDateProvider.currentLocalDate).thenReturn(today)

        subjectUnderTest = GetNonDayEventListUseCase(
            dateProvider = mockDateProvider,
            getEventList = mockGetEventListUseCase
        )
    }

    @Test
    fun `assert day event list is empty`() = runTest {
        val eventDate = today
        val events = listOf(
            Event(
                id = "1",
                name = "event",
                dayOfMonth = eventDate.dayOfMonth,
                monthOfYear = eventDate.monthValue,
                year = eventDate.year,
                currentYearOccurrence = eventDate,
                nextOccurrence = eventDate.plusYears(1),
            )
        )
        val eventFlow = flowOf(events)
        whenever(mockGetEventListUseCase.execute()).thenReturn(eventFlow)

        val result: List<Event> = subjectUnderTest.execute()

        assert(result.isEmpty())
    }

    @Test
    fun `assert day event list is not empty`() = runTest {
        val eventDate = today.plusDays(1)
        val events = listOf(
            Event(
                id = "1",
                name = "event",
                dayOfMonth = eventDate.dayOfMonth,
                monthOfYear = eventDate.monthValue,
                year = eventDate.year,
                currentYearOccurrence = eventDate,
                nextOccurrence = eventDate,
            )
        )
        val eventFlow = flowOf(events)
        whenever(mockGetEventListUseCase.execute()).thenReturn(eventFlow)

        val result: List<Event> = subjectUnderTest.execute()

        assert(result.isNotEmpty())
        assert(events.first() == result.first())
    }
}