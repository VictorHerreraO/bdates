package com.soyvictorherrera.bdates.modules.eventList.data.repository

import com.google.common.truth.Truth.assertThat
import com.soyvictorherrera.bdates.core.arch.Mapper
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.assets.AssetEventDatasourceContract
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.local.EventEntity
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.local.LocalEventDataSourceContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
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
    private lateinit var assetsDataSource: AssetEventDatasourceContract

    @Mock
    private lateinit var localDataSource: LocalEventDataSourceContract

    @Mock
    private lateinit var localMapper: Mapper<EventEntity, Event>

    private lateinit var events: EventRepository

    @Before
    fun setup() {
        events = EventRepository(
            assetsDataSource = assetsDataSource,
            localDataSource = localDataSource,
            localMapper = localMapper
        )
    }

    @Test
    fun get_event_list() = runBlocking {
        val expectedEvent = Event(
            id = "event-id",
            circleId = "circle-id",
            name = "event name",
            dayOfMonth = 31,
            monthOfYear = 12,
            year = null
        )
        val expectedList = listOf(expectedEvent)
        whenever(assetsDataSource.getEventList()).thenReturn(expectedList)

        val result = events.getEventList()

        assertThat(result).isNotNull()
        assertThat(result).isNotEmpty()
        assertThat(result).hasSize(expectedList.size)
        assertThat(result.first()).isEqualTo(expectedEvent)
    }

    @Test(expected = RuntimeException::class)
    fun get_event_list_error_propagates(): Unit = runBlocking {
        whenever(assetsDataSource.getEventList()).thenThrow(RuntimeException::class.java)

        events.getEventList()
    }

}
