package com.soyvictorherrera.bdates.modules.eventList.domain.usecase

import com.soyvictorherrera.bdates.core.date.DateProviderContract
import com.soyvictorherrera.bdates.test.data.event
import java.time.LocalDate
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
        val tomorrow = today.plusDays(1)
        val tomorrowEvent = event(withDate = tomorrow).copy(
            id = tomorrow.toString(),
            currentYearOccurrence = tomorrow,
            nextOccurrence = tomorrow,
        )
        val nextWeek = today.plusWeeks(1)
        val upcomingEvent = event(withDate = nextWeek).copy(
            id = nextWeek.toString(),
            currentYearOccurrence = nextWeek,
            nextOccurrence = nextWeek,
        )
        val eventList = listOf(tomorrowEvent, upcomingEvent)

        whenever(mockGetEventList.execute()).thenReturn(eventList)

        val result = subjectUnderTest.execute()

        assertEquals(1, result.size)
        assertEquals(upcomingEvent, result.first())
    }

    @Test
    fun `assert event list is empty`() = runBlocking {
        val tomorrow = today.plusDays(1)
        val tomorrowEvent = event(withDate = tomorrow).copy(
            id = tomorrow.toString(),
            currentYearOccurrence = tomorrow,
            nextOccurrence = tomorrow,
        )
        val dayAfterTomorrow = today.plusDays(2)
        val dayAfterTomorrowEvent = event(withDate = dayAfterTomorrow).copy(
            id = dayAfterTomorrow.toString(),
            currentYearOccurrence = dayAfterTomorrow,
            nextOccurrence = dayAfterTomorrow,
        )
        val eventList = listOf(tomorrowEvent, dayAfterTomorrowEvent)

        whenever(mockGetEventList.execute()).thenReturn(eventList)

        val result = subjectUnderTest.execute()

        assert(result.isEmpty())
    }

    @Test
    fun `assert event list contains all events`() = runBlocking {
        val upcomingDate = today.plusWeeks(1)
        val upcomingEvent1 = event().copy(
            currentYearOccurrence = upcomingDate,
            nextOccurrence = upcomingDate,
        )
        val upcomingEvent2 = event().copy(
            currentYearOccurrence = upcomingDate,
            nextOccurrence = upcomingDate,
        )
        val eventList = listOf(upcomingEvent1, upcomingEvent2)

        whenever(mockGetEventList.execute()).thenReturn(eventList)

        val result = subjectUnderTest.execute()

        assertEquals(eventList.size, result.size)
        assert(result.contains(upcomingEvent1))
        assert(result.contains(upcomingEvent2))
    }
}