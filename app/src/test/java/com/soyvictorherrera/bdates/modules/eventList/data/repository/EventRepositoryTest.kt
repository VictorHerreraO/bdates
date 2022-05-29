package com.soyvictorherrera.bdates.modules.eventList.data.repository

import com.google.common.truth.Truth.assertThat
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.EventDataSourceContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class EventRepositoryTest {

    @Mock
    private lateinit var mockDatasource: EventDataSourceContract

    private lateinit var events: EventRepository

    @Before
    fun setup() {
        events = EventRepository(mockDatasource)
    }

    @Test
    fun get_event_list() = runBlocking {
        val expectedEvent = Event(
            id = "event-id",
            name = "event name",
            dayOfMonth = 31,
            monthOfYear = 12,
            year = null
        )
        val expectedList = listOf(expectedEvent)
        val expectedFlow = flowOf(expectedList)
        whenever(mockDatasource.getEventList()).thenReturn(expectedFlow)

        val result = events.getEventList().first()

        assertThat(result).isNotNull()
        assertThat(result).isNotEmpty()
        assertThat(result).hasSize(expectedList.size)
        assertThat(result.first()).isEqualTo(expectedEvent)
    }

    @Test(expected = RuntimeException::class)
    fun get_event_list_error_propagates(): Unit = runBlocking {
        val expectedFlow = flow<List<Event>> { throw RuntimeException() }
        whenever(mockDatasource.getEventList()).thenReturn(expectedFlow)

        events.getEventList().first()
    }

}
