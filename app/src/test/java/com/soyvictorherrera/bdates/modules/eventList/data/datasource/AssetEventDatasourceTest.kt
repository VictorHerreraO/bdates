package com.soyvictorherrera.bdates.modules.eventList.data.datasource

import com.google.common.truth.Truth.assertThat
import com.soyvictorherrera.bdates.core.arch.Mapper
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.assets.AssetEventDatasource
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.assets.AssetFileManagerContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import com.soyvictorherrera.bdates.test.data.event
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class AssetEventDatasourceTest {

    @Mock
    private lateinit var mockAssets: AssetFileManagerContract

    @Mock
    private lateinit var mockMapper: Mapper<JSONObject, Event>

    private lateinit var subjectUnderTest: AssetEventDatasource

    @Before
    fun setup() {

        subjectUnderTest = AssetEventDatasource(
            assets = mockAssets,
            jsonToEventMapper = mockMapper
        )
    }

    @Test
    fun get_event_list() = runBlocking {
        val expectedEvent = event()
        val arrayString = "[{}]"

        whenever(mockMapper.map(any())).thenReturn(expectedEvent)
        whenever(mockAssets.openAsString(anyString())).thenReturn(arrayString)

        val events = subjectUnderTest.getEventList()

        assertThat(events).isNotEmpty()
        assertThat(events).hasSize(1)
        assertThat(events.first()).isEqualTo(expectedEvent)
    }

    @Test
    fun get_empty_event_list_when_file_read_fails() = runBlocking {
        val expectedException = RuntimeException("error reading file")
        whenever(mockAssets.openAsString(anyString())).thenThrow(expectedException)

        val events = subjectUnderTest.getEventList()

        assertThat(events).isEmpty()
    }
}
