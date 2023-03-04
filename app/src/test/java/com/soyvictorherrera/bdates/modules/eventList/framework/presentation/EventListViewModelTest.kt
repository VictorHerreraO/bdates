package com.soyvictorherrera.bdates.modules.eventList.framework.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.soyvictorherrera.bdates.core.date.DateProviderContract
import com.soyvictorherrera.bdates.core.resource.ResourceManagerContract
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.FilterEventListArgs
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.FilterEventListUseCaseContract
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.GetDayEventListUseCaseContract
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.GetNonDayEventListUseCaseContract
import com.soyvictorherrera.bdates.test.data.event
import com.soyvictorherrera.bdates.util.MainCoroutineRule
import com.soyvictorherrera.bdates.util.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import java.time.LocalDate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EventListViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val dateProvider = mockk<DateProviderContract>()
    private val resourceManager = mockk<ResourceManagerContract>()
    private val getDayEventList = mockk<GetDayEventListUseCaseContract>()
    private val getNonDayEventList = mockk<GetNonDayEventListUseCaseContract>()
    private val filterEventListUseCase = mockk<FilterEventListUseCaseContract>()

    private lateinit var subjectUnderTest: EventListViewModel

    private val today = LocalDate.now()

    @Before
    fun setup() {
        every { dateProvider.currentLocalDate } returns today
        every { resourceManager.getString(any<String>()) } returns "string"
        every { resourceManager.getString(any<String>(), any()) } returns "string"

        subjectUnderTest = EventListViewModel(
            dateProvider = dateProvider,
            resourceManager = resourceManager,
            getDayEventList = getDayEventList,
            getNonDayEventList = getNonDayEventList,
            filterEventListUseCase = filterEventListUseCase
        )
    }

    @Test
    fun `assert that events get loaded on init`() = runTest {
        val tomorrow = today.plusDays(1)
        val events = listOf(
            event(withDate = tomorrow).copy(
                nextOccurrence = tomorrow
            )
        )
        coEvery { getNonDayEventList.execute() } returns (events)
        coEvery { getDayEventList.execute() } returns (emptyList())
        coEvery { filterEventListUseCase.execute(any()) } returns Result.success(events)

        advanceUntilIdle()
        val result: List<EventViewState> = subjectUnderTest.events.getOrAwaitValue()

        assert(result.isNotEmpty())
        assertEquals(events.first().id, result.first().id)
    }

    @Test
    fun `assert that day events get loaded on init`() = runTest {
        val events = listOf(
            event(withDate = today).copy(
                currentYearOccurrence = today,
                nextOccurrence = today
            )
        )
        coEvery { getDayEventList.execute() } returns events
        coEvery { getNonDayEventList.execute() } returns emptyList()
        coEvery { filterEventListUseCase.execute(any()) } returns Result.success(events)

        advanceUntilIdle()
        val result: List<TodayEventViewState> = subjectUnderTest.todayEvents.getOrAwaitValue()

        assert(result.isNotEmpty())
        assertEquals(events.first().id, result.first().id)
    }

    @Test
    fun `verify on query text changed calls use case filter use case`(): Unit = runTest {
        val expectedQuery = "query"
        val slot = slot<FilterEventListArgs>()

        coEvery { filterEventListUseCase.execute(capture(slot)) } returns Result.success(
            emptyList()
        )

        subjectUnderTest.onQueryTextChanged(expectedQuery)
        advanceUntilIdle()

        coVerify(exactly = 1) { filterEventListUseCase.execute(any()) }
        assertThat(slot.captured.query).isEqualTo(expectedQuery)
    }

    @Test
    fun `verify refresh calls get use cases`(): Unit = runTest {
        coEvery { getDayEventList.execute() } returns emptyList()
        coEvery { getNonDayEventList.execute() } returns emptyList()
        coEvery { filterEventListUseCase.execute(any()) } returns Result.success(emptyList())

        subjectUnderTest.refresh()
        advanceUntilIdle()

        // Called on view model init and on refresh
        coVerify(exactly = 2) { getDayEventList.execute() }
        coVerify(exactly = 2) { getNonDayEventList.execute() }
        coVerify(exactly = 2) { filterEventListUseCase.execute(any()) }
    }

}
