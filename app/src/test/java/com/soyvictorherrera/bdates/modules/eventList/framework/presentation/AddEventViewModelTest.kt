package com.soyvictorherrera.bdates.modules.eventList.framework.presentation

import com.google.common.truth.Truth.assertThat
import com.soyvictorherrera.bdates.core.date.DateProviderContract
import com.soyvictorherrera.bdates.core.navigation.NavigationEvent
import com.soyvictorherrera.bdates.core.persistence.OnCreated
import com.soyvictorherrera.bdates.modules.circles.data.preferences.CirclePreferencesContract
import com.soyvictorherrera.bdates.modules.eventList.data.repository.EventRepositoryContract
import com.soyvictorherrera.bdates.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
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
    private val circlePreferences = mockk<CirclePreferencesContract>()

    private lateinit var subjectUnderTest: AddEventViewModel

    private val today = LocalDate.now()

    @Before
    fun setup() {
        every { dateProvider.currentLocalDate } returns today

        subjectUnderTest = AddEventViewModel(
            dateProvider,
            eventRepository,
            circlePreferences
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
    fun `verify on action click`(): Unit = runTest {
        val localCircleId = "circle-id"
        val expectedId = "event-id"
        val initialNavigationEvent = subjectUnderTest.navigation.value

        every { circlePreferences.localCircleId } returns localCircleId
        coEvery { eventRepository.createEvent(any(), any()) } answers {
            secondArg<OnCreated?>()?.invoke(expectedId)
        }

        subjectUnderTest.onActionClick()
        advanceUntilIdle()

        val loadingState = subjectUnderTest.state.value
        assertThat(loadingState.isSaveEnabled).isFalse()

        val finalNavigationEvent = subjectUnderTest.navigation.value
        coVerify(exactly = 1) { eventRepository.createEvent(any(), any()) }
        assertThat(finalNavigationEvent).isNotEqualTo(initialNavigationEvent)
        assertThat(finalNavigationEvent?.consumed).isFalse()
        assertThat(finalNavigationEvent).isInstanceOf(NavigationEvent.NavigateBack::class.java)
    }

}