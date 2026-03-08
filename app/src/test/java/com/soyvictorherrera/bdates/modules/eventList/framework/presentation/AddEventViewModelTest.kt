package com.soyvictorherrera.bdates.modules.eventList.framework.presentation

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import com.soyvictorherrera.bdates.core.date.DateProviderContract
import com.soyvictorherrera.bdates.core.event.NavigationEvent
import com.soyvictorherrera.bdates.modules.eventList.data.repository.EventRepositoryContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.UpsertEventUseCaseContract
import com.soyvictorherrera.bdates.test.data.event
import com.soyvictorherrera.bdates.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import java.time.LocalDate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class AddEventViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val dateProvider = mockk<DateProviderContract>()
    private val eventRepository = mockk<EventRepositoryContract>()
    private val upsertEventUseCase = mockk<UpsertEventUseCaseContract>()

    private lateinit var subjectUnderTest: AddEventViewModel

    private val today = LocalDate.now()

    @Before
    fun setup() {
        every { dateProvider.currentLocalDate } returns today

        subjectUnderTest = AddEventViewModel(
            stateHandle = SavedStateHandle(),
            dateProvider = dateProvider,
            eventRepository = eventRepository,
            upsertEventUseCase = upsertEventUseCase
        )
    }

    @Test
    fun `assert state on event name change`() {
        val expected = "event name"
        val initialState = subjectUnderTest.state.value

        subjectUnderTest.onEventNameChange(expected)

        val finalState = subjectUnderTest.state.value

        assertThat(finalState).isNotEqualTo(initialState)
        assertThat(finalState.eventName).isEqualTo(expected)
    }

    @Test
    fun `assert state on date selected`() {
        val expected = today.plusDays(1)
        val initialState = subjectUnderTest.state.value

        subjectUnderTest.onDateSelected(expected)

        val finalState = subjectUnderTest.state.value

        assertThat(finalState).isNotEqualTo(initialState)
        assertThat(finalState.selectedDate).isEqualTo(expected)
    }

    @Test
    fun `assert state on year selected`() {
        val expected = today.plusYears(1).year
        val initialState = subjectUnderTest.state.value

        subjectUnderTest.onYearSelected(expected)

        val finalState = subjectUnderTest.state.value

        assertThat(finalState).isNotEqualTo(initialState)
        assertThat(finalState.selectedDate.year).isEqualTo(expected)
    }

    @Test
    fun `assert state on year cleared`() {
        val initialDate = today
            .plusDays(1)
            .plusMonths(1)
            .plusYears(1)
        subjectUnderTest.onDateSelected(initialDate)

        val initialState = subjectUnderTest.state.value

        subjectUnderTest.onYearCleared()

        val finalState = subjectUnderTest.state.value

        assertThat(finalState).isNotEqualTo(initialState)
        assertThat(finalState.selectedDate.dayOfMonth).isEqualTo(initialDate.dayOfMonth)
        assertThat(finalState.selectedDate.monthValue).isEqualTo(initialDate.monthValue)
        assertThat(finalState.selectedDate.year).isEqualTo(today.year)
        assertThat(finalState.selectedYear).isNull()
    }

    @Test
    fun `assert state on year disabled toggle`() {
        val initialState = subjectUnderTest.state.value

        // Disable
        subjectUnderTest.onYearDisabled(true)

        val disabledState = subjectUnderTest.state.value
        assertThat(disabledState).isNotEqualTo(initialState)
        assertThat(disabledState.isYearDisabled).isTrue()

        // Enable
        subjectUnderTest.onYearDisabled(false)

        val enabledState = subjectUnderTest.state.value
        assertThat(enabledState).isNotEqualTo(disabledState)
        assertThat(enabledState.isYearDisabled).isFalse()
    }

    @Test
    fun `assert state when no eventId is provided is CREATE`() {
        val stateHandle = SavedStateHandle()
        val subjectUnderTest = AddEventViewModel(
            stateHandle, dateProvider, eventRepository, upsertEventUseCase
        )

        val state = subjectUnderTest.state.value

        assertThat(state.editMode).isEqualTo(EditMode.CREATE)
    }

    @Test
    fun `assert state when eventId is provided is EDIT`() {
        val expectedId = "event-id"
        val stateHandle = SavedStateHandle(mapOf("eventId" to expectedId))
        val subjectUnderTest = AddEventViewModel(
            stateHandle, dateProvider, eventRepository, upsertEventUseCase
        )

        val state = subjectUnderTest.state.value

        assertThat(state.editMode).isEqualTo(EditMode.EDIT)
    }

    @Test
    fun `assert state when event is loaded`() = runTest {
        val expectedEvent = event()
        val expectedDate = LocalDate.of(
            expectedEvent.year!!,
            expectedEvent.monthOfYear,
            expectedEvent.dayOfMonth
        )
        val stateHandle = SavedStateHandle(mapOf("eventId" to expectedEvent.id))

        coEvery { eventRepository.getEvent(any()) } returns expectedEvent

        val subjectUnderTest = AddEventViewModel(
            stateHandle, dateProvider, eventRepository, upsertEventUseCase
        )
        advanceUntilIdle()
        val state = subjectUnderTest.state.value

        assertThat(state.eventName).isEqualTo(expectedEvent.name)
        assertThat(state.selectedDate).isEqualTo(expectedDate)
        assertThat(state.selectedYear).isEqualTo(expectedEvent.year)
        assertThat(state.isYearDisabled).isFalse()
    }

    @Test
    fun `verify on action click saves event`(): Unit = runTest {
        val initialNavigationValue = subjectUnderTest.navigation.value

        coEvery { upsertEventUseCase.execute(any()) } just runs

        subjectUnderTest.onEventNameChange("Test Event")
        subjectUnderTest.onActionClick()
        advanceUntilIdle()

        val loadingState = subjectUnderTest.state.value
        assertThat(loadingState.isSaveEnabled).isFalse()

        val finalNavigationValue = subjectUnderTest.navigation.value
        coVerify(exactly = 1) { upsertEventUseCase.execute(any()) }
        assertThat(finalNavigationValue).isNotEqualTo(initialNavigationValue)
        assertThat(finalNavigationValue).isEqualTo(NavigationEvent.NavigateBack)
    }

    @Test
    fun `verify on action click updates event`(): Unit = runTest {
        val expectedEvent = event()
        val stateHandle = SavedStateHandle(mapOf("eventId" to expectedEvent.id))
        val initialNavigationValue = subjectUnderTest.navigation.value

        val slot = slot<Event>()
        coEvery { eventRepository.getEvent(any()) } returns expectedEvent
        coEvery { upsertEventUseCase.execute(capture(slot)) } just runs

        val subjectUnderTest = AddEventViewModel(
            stateHandle, dateProvider, eventRepository, upsertEventUseCase
        )
        advanceUntilIdle()
        subjectUnderTest.onEventNameChange("Test Event")
        subjectUnderTest.onActionClick()
        advanceUntilIdle()

        val loadingState = subjectUnderTest.state.value
        assertThat(loadingState.isSaveEnabled).isFalse()

        val finalNavigationValue = subjectUnderTest.navigation.value
        coVerify(exactly = 1) { upsertEventUseCase.execute(any()) }
        assertThat(slot.captured.id).isEqualTo(expectedEvent.id)
        assertThat(slot.captured.circleId).isEqualTo(expectedEvent.circleId)
        assertThat(finalNavigationValue).isNotEqualTo(initialNavigationValue)
        assertThat(finalNavigationValue).isEqualTo(NavigationEvent.NavigateBack)
    }

    @Test
    fun `verify on delete click deletes event`() = runTest {
        val expectedEvent = event()
        val stateHandle = SavedStateHandle(mapOf("eventId" to expectedEvent.id))
        val subjectUnderTest = AddEventViewModel(
            stateHandle, dateProvider, eventRepository, upsertEventUseCase
        )
        val initialNavigationValue = subjectUnderTest.navigation.value

        coEvery { eventRepository.deleteEvent(any()) } just runs

        subjectUnderTest.onDeleteClick()
        advanceUntilIdle()

        val finalNavigationValue = subjectUnderTest.navigation.value
        coVerify(exactly = 1) { eventRepository.deleteEvent(eq(expectedEvent.id!!)) }
        assertThat(finalNavigationValue).isNotEqualTo(initialNavigationValue)
        assertThat(finalNavigationValue).isEqualTo(NavigationEvent.NavigateBack)
    }
}