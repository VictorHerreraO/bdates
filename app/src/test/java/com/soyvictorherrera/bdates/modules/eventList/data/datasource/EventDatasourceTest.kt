package com.soyvictorherrera.bdates.modules.eventList.data.datasource

import com.google.common.truth.Truth.assertThat
import com.soyvictorherrera.bdates.core.arch.Mapper
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class EventDatasourceTest {

    @Mock
    private lateinit var mockAssets: AssetFileManagerContract

    @Mock
    private lateinit var mockMapper: Mapper<JSONObject, Event>

    private lateinit var datasource: EventDatasource

    private val expectedEvent = Event(
        id = "id-event",
        name = "event-name",
        dayOfMonth = 1,
        monthOfYear = 1,
        year = 1970
    )

    @Before
    fun setup() {
        whenever(mockMapper.map(any())).thenReturn(expectedEvent)

        datasource = EventDatasource(
            assets = mockAssets,
            jsonToEventMapper = mockMapper
        )
    }

    @Test
    fun get_event_list() = runBlocking {
        val arrayString = "[{}]"
        whenever(mockAssets.openAsString(anyString())).thenReturn(arrayString)

        val events = datasource.getEventList().first()

        assertThat(events).isNotEmpty()
        assertThat(events).hasSize(1)
        assertThat(events.first()).isEqualTo(expectedEvent)
    }

    @Test
    fun get_empty_event_list_when_file_read_fails() = runBlocking {
        val expectedException = RuntimeException("error reading file")
        whenever(mockAssets.openAsString(anyString())).thenThrow(expectedException)

        val events = datasource.getEventList().first()

        assertThat(events).isEmpty()
    }

}
