package com.soyvictorherrera.bdates.modules.notifications.delegate

import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.GetDayEventListUseCaseContract
import com.soyvictorherrera.bdates.modules.notifications.NotificationManagerContract
import java.time.LocalDate
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class DayNotificationDelegateTest {

    @Mock
    private lateinit var mockNotificationManager: NotificationManagerContract

    @Mock
    private lateinit var mockGetDayEventList: GetDayEventListUseCaseContract

    private lateinit var subjectUnderTest: DayNotificationDelegate

    private val today = LocalDate.of(2023, 7, 1)

    @Before
    fun setup() {
        subjectUnderTest = DayNotificationDelegate(
            notificationManager = mockNotificationManager,
            getDayEventList = mockGetDayEventList
        )
    }

    @Test
    fun `verify show events reminder is called when event list is not empty`() = runBlocking {
        val eventDate = today
        val event = Event(
            id = "1",
            circleId = "circle-id",
            name = "notification event",
            dayOfMonth = eventDate.dayOfMonth,
            monthOfYear = eventDate.monthValue,
            year = eventDate.year,
            currentYearOccurrence = eventDate,
            nextOccurrence = eventDate.plusYears(1)
        )
        val eventList = listOf(event)

        whenever(mockGetDayEventList.execute()).thenReturn(eventList)

        subjectUnderTest.notifyDayEvents()

        verify(mockNotificationManager).showDayEventsReminder(eq(1), eq(event.name))
    }

    @Test
    fun `verify show events reminder is not called when event list is empty`() = runBlocking {
        val emptyList = emptyList<Event>()

        whenever(mockGetDayEventList.execute()).thenReturn(emptyList)

        subjectUnderTest.notifyDayEvents()

        verify(mockNotificationManager, never()).showDayEventsReminder(any(), anyString())
    }
}