package com.soyvictorherrera.bdates.modules.eventList.framework.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.soyvictorherrera.bdates.core.arch.UseCase
import com.soyvictorherrera.bdates.core.resource.ResourceManagerContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.FilterEventListArgs
import com.soyvictorherrera.bdates.util.MainCoroutineRule
import com.soyvictorherrera.bdates.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class EventListViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var mockResources: ResourceManagerContract

    @Mock
    lateinit var mockGetEventListUseCase: UseCase<Unit, Flow<List<Event>>>

    @Mock
    lateinit var mockFilterEventListUseCase: UseCase<FilterEventListArgs, Result<List<Event>>>

    private lateinit var subjectUnderTest: EventListViewModel

    private val today = LocalDate.now()

    @Before
    fun setup() {
        whenever(mockResources.getString(any(), any())).thenReturn("string")

        subjectUnderTest = EventListViewModel(
            resourceManager = mockResources,
            getEventListUseCase = mockGetEventListUseCase,
            filterEventListUseCase = mockFilterEventListUseCase
        )
    }

    @Test
    fun `assert that events get loaded on init`() = runTest {
        val tomorrow = today.plusDays(1)
        val events = listOf(
            Event(
                id = "1",
                name = "Test",
                dayOfMonth = tomorrow.dayOfMonth,
                monthOfYear = tomorrow.monthValue,
                year = tomorrow.year,
                nextOccurrence = tomorrow
            )
        )
        whenever(mockGetEventListUseCase.invoke(Unit)).thenReturn(flowOf(events))
        whenever(mockFilterEventListUseCase.invoke(any())).then { Result.success(events) }

        advanceUntilIdle()
        val result: List<EventViewState> = subjectUnderTest.events.getOrAwaitValue()

        assert(result.isNotEmpty())
        assertEquals(events.first().id, result.first().id)
    }

    @Test
    fun `assert that day events get loaded on init`() = runTest {
        val events = listOf(
            Event(
                id = "1",
                name = "Test",
                dayOfMonth = today.dayOfMonth,
                monthOfYear = today.monthValue,
                year = today.year,
                nextOccurrence = today
            )
        )
        whenever(mockGetEventListUseCase.invoke(Unit)).thenReturn(flowOf(events))
        whenever(mockFilterEventListUseCase.invoke(any())).then { Result.success(events) }

        advanceUntilIdle()
        val result: List<TodayEventViewState> = subjectUnderTest.todayEvents.getOrAwaitValue()

        assert(result.isNotEmpty())
        assertEquals(events.first().id, result.first().id)
    }

}
