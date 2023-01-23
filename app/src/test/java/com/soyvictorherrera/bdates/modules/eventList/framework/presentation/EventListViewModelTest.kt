package com.soyvictorherrera.bdates.modules.eventList.framework.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.soyvictorherrera.bdates.core.resource.ResourceManagerContract
import com.soyvictorherrera.bdates.modules.date.DateProviderContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.FilterEventListUseCaseContract
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.GetDayEventListUseCaseContract
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.GetNonDayEventListUseCaseContract
import com.soyvictorherrera.bdates.util.MainCoroutineRule
import com.soyvictorherrera.bdates.util.getOrAwaitValue
import java.time.LocalDate
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class EventListViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var mockDateProvider: DateProviderContract

    @Mock
    lateinit var mockResources: ResourceManagerContract

    @Mock
    lateinit var mockGetDayEventList: GetDayEventListUseCaseContract

    @Mock
    lateinit var mockGetNonDayEventList: GetNonDayEventListUseCaseContract

    @Mock
    lateinit var mockFilterEventListUseCase: FilterEventListUseCaseContract

    private lateinit var subjectUnderTest: EventListViewModel

    private val today = LocalDate.now()

    @Before
    fun setup() {
        whenever(mockDateProvider.currentLocalDate).thenReturn(today)
        whenever(mockResources.getString(any(), any())).thenReturn("string")

        subjectUnderTest = EventListViewModel(
            dateProvider = mockDateProvider,
            resourceManager = mockResources,
            getDayEventList = mockGetDayEventList,
            getNonDayEventList = mockGetNonDayEventList,
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
        whenever(mockGetNonDayEventList.execute()).thenReturn(events)
        whenever(mockGetDayEventList.execute()).thenReturn(emptyList())
        whenever(mockFilterEventListUseCase.execute(any())).then { Result.success(events) }

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
                currentYearOccurrence = today,
                nextOccurrence = today
            )
        )
        whenever(mockGetDayEventList.execute()).thenReturn(events)
        whenever(mockGetNonDayEventList.execute()).thenReturn(emptyList())
        whenever(mockFilterEventListUseCase.execute(any())).then { Result.success(events) }

        advanceUntilIdle()
        val result: List<TodayEventViewState> = subjectUnderTest.todayEvents.getOrAwaitValue()

        assert(result.isNotEmpty())
        assertEquals(events.first().id, result.first().id)
    }

}
