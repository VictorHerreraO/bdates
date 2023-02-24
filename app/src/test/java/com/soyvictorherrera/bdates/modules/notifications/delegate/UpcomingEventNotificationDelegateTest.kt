package com.soyvictorherrera.bdates.modules.notifications.delegate

import com.soyvictorherrera.bdates.core.date.DateProviderContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.GetUpcomingEventListUseCaseContract
import com.soyvictorherrera.bdates.modules.notifications.NotificationManagerContract
import java.time.LocalDate
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class UpcomingEventNotificationDelegateTest {

    @Mock
    private lateinit var mockNotificationManager: NotificationManagerContract

    @Mock
    private lateinit var mockGetUpcomingEventList: GetUpcomingEventListUseCaseContract

    @Mock
    private lateinit var mockDateProvider: DateProviderContract

    private lateinit var subjectUnderTest: UpcomingEventNotificationDelegate

    private val upcomingEventDate = LocalDate.of(2023, 7, 1)

    private val formattedDate = "date"

    @Before
    fun setup() {
        whenever(mockDateProvider.formatDateAsDay(any())).thenReturn(formattedDate)

        subjectUnderTest = UpcomingEventNotificationDelegate(
            notificationManager = mockNotificationManager,
            getUpcomingEventList = mockGetUpcomingEventList,
            dateProvider = mockDateProvider
        )
    }

    @Test
    fun `verify show upcoming events reminder is called when event list is not empty`(

    ) = runBlocking {
        val eventDate = upcomingEventDate
        val event = Event(
            id = "1",
            circleId = "circle-id",
            name = "notification event",
            dayOfMonth = eventDate.dayOfMonth,
            monthOfYear = eventDate.monthValue,
            year = eventDate.year,
            currentYearOccurrence = eventDate,
            nextOccurrence = eventDate
        )
        val eventList = listOf(event)

        whenever(mockGetUpcomingEventList.execute()).thenReturn(eventList)

        subjectUnderTest.notifyUpcomingEvents()

        verify(mockNotificationManager).showUpcomingEventsReminder(
            eq(1),
            eq(event.name),
            eq(formattedDate)
        )
    }

    @Test
    fun `verify show upcoming events reminder is not called when event list is empty`(

    ) = runBlocking {
        val emptyList = emptyList<Event>()

        whenever(mockGetUpcomingEventList.execute()).thenReturn(emptyList)

        subjectUnderTest.notifyUpcomingEvents()

        verify(mockNotificationManager, never()).showUpcomingEventsReminder(any(), any(), any())
    }

}