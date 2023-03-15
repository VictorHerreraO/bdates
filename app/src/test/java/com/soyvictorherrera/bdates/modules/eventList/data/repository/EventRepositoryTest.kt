package com.soyvictorherrera.bdates.modules.eventList.data.repository

import com.google.common.truth.Truth.assertThat
import com.soyvictorherrera.bdates.core.arch.Mapper
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.assets.AssetEventDatasourceContract
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.local.EventEntity
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.local.LocalEventDataSourceContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import com.soyvictorherrera.bdates.test.data.event
import com.soyvictorherrera.bdates.test.data.eventEntity
import com.soyvictorherrera.bdates.test.data.eventEntityBar
import com.soyvictorherrera.bdates.test.data.eventModelBar
import com.soyvictorherrera.bdates.test.data.eventModelFoo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EventRepositoryTest {

    private val assetsDataSource = mockk<AssetEventDatasourceContract>()

    private val localDataSource = mockk<LocalEventDataSourceContract>()

    private val localMapper = mockk<Mapper<EventEntity, Event>>()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var subjectUnderTest: EventRepository

    @Before
    fun setup() {
        subjectUnderTest = EventRepository(
            assetsDataSource = assetsDataSource,
            localDataSource = localDataSource,
            localMapper = localMapper,
            ioDispatcher = testDispatcher
        )
    }

    @Test
    fun get_event_list() = runBlocking {
        val expectedAssetEvent = eventModelFoo()
        val localEvent = eventEntityBar()
        val expectedEvent = eventModelBar()
        val expectedList = listOf(expectedAssetEvent, expectedEvent)

        coEvery { assetsDataSource.getEventList() } returns listOf(expectedAssetEvent)
        coEvery { localDataSource.getEventList() } returns listOf(localEvent)
        every { localMapper.map(any()) } returns expectedEvent

        val result = subjectUnderTest.getEventList()

        assertThat(result).isNotNull()
        assertThat(result).isNotEmpty()
        assertThat(result).hasSize(expectedList.size)
        assertThat(result).contains(expectedAssetEvent)
        assertThat(result).contains(expectedEvent)
    }

    @Test(expected = RuntimeException::class)
    fun get_event_list_error_propagates(): Unit = runBlocking {
        coEvery { assetsDataSource.getEventList() } throws RuntimeException()

        subjectUnderTest.getEventList()
    }

    @Test
    fun `assert get event`() = runTest {
        val eventEntity = eventEntity()
        val expectedEvent = event()
        val expectedId = expectedEvent.id!!

        coEvery { localDataSource.getEvent(any()) } returns eventEntity
        every { localMapper.map(any()) } returns expectedEvent

        val result = subjectUnderTest.getEvent(expectedId)

        assertThat(result).isEqualTo(expectedEvent)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `expect illegal argument exception when no event found`() = runTest {
        val expectedId = "event-id"
        coEvery { localDataSource.getEvent(any()) } returns null

        subjectUnderTest.getEvent(expectedId)
    }

    @Test
    fun `verify create event`() = runTest {
        val expectedId = "expected-id"
        val event = event().copy(id = "")

        every { localMapper.reverseMap(any()) } returns eventEntity()
        coEvery { localDataSource.createEvent(any()) } returns expectedId

        val result: String = subjectUnderTest.createEvent(event)

        assertThat(result).isEqualTo(expectedId)
        coVerify(exactly = 1) { localDataSource.createEvent(any()) }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `expect IllegalArgumentException when create event with id`() = runTest {
        val event = event()

        subjectUnderTest.createEvent(event)
    }

    @Test
    fun `verify update event`() = runTest {
        val event = event()
        val expectedEntity = eventEntity()

        every { localMapper.reverseMap(any()) } returns expectedEntity
        coEvery { localDataSource.updateEvent(any()) } just runs

        subjectUnderTest.updateEvent(event)

        coVerify(exactly = 1) { localDataSource.updateEvent(expectedEntity) }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `expect illegal argument exception when update event without id`() = runTest {
        val event = event().copy(id = null)

        subjectUnderTest.updateEvent(event)
    }

    @Test
    fun `verify delete event`() = runTest {
        val eventId = "event-id"

        coEvery { localDataSource.deleteEvent(any()) } just runs

        subjectUnderTest.deleteEvent(eventId)

        coVerify(exactly = 1) { localDataSource.deleteEvent(eventId) }
    }
}
