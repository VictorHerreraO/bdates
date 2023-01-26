package com.soyvictorherrera.bdates.modules.eventList.domain.usecase

import com.soyvictorherrera.bdates.modules.date.DateProviderContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import java.time.LocalDate
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class GetUpcomingEventListUseCaseTest {
    @Mock
    private lateinit var mockDateProvider: DateProviderContract

    @Mock
    private lateinit var mockGetEventList: GetEventListUseCaseContract

    private lateinit var subjectUnderTest: GetUpcomingEventListUseCase

    private val today = LocalDate.of(2023, 7, 1)

    @Before
    fun setup() {
        whenever(mockDateProvider.currentLocalDate).thenReturn(today)

        subjectUnderTest = GetUpcomingEventListUseCase(
            mockDateProvider,
            mockGetEventList
        )
    }

    @Test
    fun `assert event list contains expected event`() = runBlocking {
        val tomorrowEvent = today.plusDays(1).run {
            Event(
                id = toString(),
                name = "today event",
                dayOfMonth = dayOfMonth,
                monthOfYear = monthValue,
                year = year,
                currentYearOccurrence = this,
                nextOccurrence = this,
            )
        }
        val upcomingEvent = today.plusWeeks(1).run {
            Event(
                id = toString(),
                name = "next week event",
                dayOfMonth = dayOfMonth,
                monthOfYear = monthValue,
                year = year,
                currentYearOccurrence = this,
                nextOccurrence = this,
            )
        }
        val eventList = listOf(tomorrowEvent, upcomingEvent)
        val eventFlow = flowOf(eventList)

        whenever(mockGetEventList.execute()).thenReturn(eventFlow)

        val result = subjectUnderTest.execute()

        assertEquals(1, result.size)
        assertEquals(upcomingEvent, result.first())
    }

    @Test
    fun `assert event list is empty`() = runBlocking {
        val tomorrowEvent = today.plusDays(1).run {
            Event(
                id = toString(),
                name = "today event",
                dayOfMonth = dayOfMonth,
                monthOfYear = monthValue,
                year = year,
                currentYearOccurrence = this,
                nextOccurrence = this,
            )
        }
        val dayAfterTomorrowEvent = today.plusDays(2).run {
            Event(
                id = toString(),
                name = "day after tomorrow event",
                dayOfMonth = dayOfMonth,
                monthOfYear = monthValue,
                year = year,
                currentYearOccurrence = this,
                nextOccurrence = this,
            )
        }
        val eventList = listOf(tomorrowEvent, dayAfterTomorrowEvent)
        val eventFlow = flowOf(eventList)

        whenever(mockGetEventList.execute()).thenReturn(eventFlow)

        val result = subjectUnderTest.execute()

        assert(result.isEmpty())
    }

    @Test
    fun `assert event list contains all events`() = runBlocking {
        val upcomingEvent1 = today.plusWeeks(1).run {
            Event(
                id = toString(),
                name = "next week event 1",
                dayOfMonth = dayOfMonth,
                monthOfYear = monthValue,
                year = year,
                currentYearOccurrence = this,
                nextOccurrence = this,
            )
        }
        val upcomingEvent2 = today.plusWeeks(1).run {
            Event(
                id = toString(),
                name = "next week event 2",
                dayOfMonth = dayOfMonth,
                monthOfYear = monthValue,
                year = year,
                currentYearOccurrence = this,
                nextOccurrence = this,
            )
        }
        val eventList = listOf(upcomingEvent1, upcomingEvent2)
        val eventFlow = flowOf(eventList)

        whenever(mockGetEventList.execute()).thenReturn(eventFlow)

        val result = subjectUnderTest.execute()

        assertEquals(eventList.size, result.size)
        assert(result.contains(upcomingEvent1))
        assert(result.contains(upcomingEvent2))
    }
}