package com.soyvictorherrera.bdates.modules.eventList.domain.usecase

import com.soyvictorherrera.bdates.core.event.NavigationEvent
import com.soyvictorherrera.bdates.core.network.Resource
import com.soyvictorherrera.bdates.modules.circles.data.preferences.CirclePreferencesContract
import com.soyvictorherrera.bdates.modules.circles.data.repository.CircleRepositoryContract
import com.soyvictorherrera.bdates.modules.circles.domain.model.Circle
import com.soyvictorherrera.bdates.modules.eventList.data.repository.EventRepositoryContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class UpsertEventUseCaseTest {

    private val eventRepository = mockk<EventRepositoryContract>()
    private val circleRepository = mockk<CircleRepositoryContract>()
    private val circlePreferences = mockk<CirclePreferencesContract>(relaxed = true)

    private lateinit var subjectUnderTest: UpsertEventUseCase

    @Before
    fun setup() {
        subjectUnderTest = UpsertEventUseCase(
            eventRepository,
            circleRepository,
            circlePreferences
        )
    }

    @Test
    fun `when localCircleId is in preferences then use it and save event`() = runTest {
        val expectedCircleId = "cached-circle-id"
        val expectedEventId = "new-event-id"
        val event = Event(id = null, circleId = "", name = "Test", dayOfMonth = 1, monthOfYear = 1, year = null)

        every { circlePreferences.localCircleId } returns expectedCircleId
        coEvery { eventRepository.createEvent(any()) } returns expectedEventId

        subjectUnderTest.execute(event)

        val slot = slot<Event>()
        coVerify(exactly = 1) { eventRepository.createEvent(capture(slot)) }
        assertEquals(expectedCircleId, slot.captured.circleId)
        coVerify(exactly = 0) { circleRepository.getCircles() }
    }

    @Test
    fun `when localCircleId not in preferences but default circle exists then use its id and save event`() = runTest {
        val expectedCircleId = "device-local"
        val expectedEventId = "new-event-id"
        val event = Event(id = null, circleId = "", name = "Test", dayOfMonth = 1, monthOfYear = 1, year = null)

        every { circlePreferences.localCircleId } returns null
        coEvery { circleRepository.getCircles() } returns Resource.Success(listOf(
            Circle(
                id = expectedCircleId,
                name = "Device local circle",
                description = "",
                isDefaultCircle = true,
                isLocalOnly = true,
                updateDate = null
            )
        ))
        coEvery { eventRepository.createEvent(any()) } returns expectedEventId

        subjectUnderTest.execute(event)

        val slot = slot<Event>()
        coVerify(exactly = 1) { eventRepository.createEvent(capture(slot)) }
        assertEquals(expectedCircleId, slot.captured.circleId)
        coVerify(exactly = 1) { circlePreferences.localCircleId = expectedCircleId }
        coVerify(exactly = 0) { circleRepository.createCircle(any()) }
    }

    @Test
    fun `when localCircleId missing and no default circle exists then create circle and save event`() = runTest {
        val expectedCircleId = "new-local-circle-id"
        val expectedEventId = "new-event-id"
        val event = Event(id = null, circleId = "", name = "Test", dayOfMonth = 1, monthOfYear = 1, year = null)

        every { circlePreferences.localCircleId } returns null
        coEvery { circleRepository.getCircles() } returns Resource.Success(emptyList())
        coEvery { circleRepository.createCircle(any()) } returns expectedCircleId
        coEvery { eventRepository.createEvent(any()) } returns expectedEventId

        subjectUnderTest.execute(event)

        val eventSlot = slot<Event>()
        coVerify(exactly = 1) { eventRepository.createEvent(capture(eventSlot)) }
        assertEquals(expectedCircleId, eventSlot.captured.circleId)
        
        val circleSlot = slot<Circle>()
        coVerify(exactly = 1) { circleRepository.createCircle(capture(circleSlot)) }
        assertEquals(true, circleSlot.captured.isDefaultCircle)
        
        coVerify(exactly = 1) { circlePreferences.localCircleId = expectedCircleId }
    }

    @Test
    fun `when event id is not null then call updateEvent`() = runTest {
        val expectedCircleId = "cached-circle-id"
        val eventId = "existing-event-id"
        val event = Event(id = eventId, circleId = expectedCircleId, name = "Test", dayOfMonth = 1, monthOfYear = 1, year = null)

        every { circlePreferences.localCircleId } returns expectedCircleId
        coEvery { eventRepository.updateEvent(any()) } just runs

        subjectUnderTest.execute(event)

        coVerify(exactly = 1) { eventRepository.updateEvent(event) }
        coVerify(exactly = 0) { eventRepository.createEvent(any()) }
    }
}
