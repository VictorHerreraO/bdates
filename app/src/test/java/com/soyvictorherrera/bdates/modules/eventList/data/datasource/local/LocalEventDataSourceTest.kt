package com.soyvictorherrera.bdates.modules.eventList.data.datasource.local

import com.google.common.truth.Truth.assertThat
import com.soyvictorherrera.bdates.modules.eventList.data.mapper.EventEntityToModelMapperContract
import com.soyvictorherrera.bdates.test.data.event
import com.soyvictorherrera.bdates.test.data.eventEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class LocalEventDataSourceTest {
    private lateinit var subjectUnderTest: LocalEventDataSource

    private val eventDao: EventDao = mockk()

    private val mapper: EventEntityToModelMapperContract = mockk()

    @Before
    fun setup() {
        subjectUnderTest = LocalEventDataSource(
            dao = eventDao,
            mapper = mapper
        )
    }

    @Test
    fun `assert getGetEventList`(): Unit = runTest {
        val entity = eventEntity()
        val excludedEntity = eventEntity().copy(name = "")
        val expected = event()

        coEvery { eventDao.getAll() } returns listOf(entity, excludedEntity)
        every { mapper.map(any()) } returns expected

        val result = subjectUnderTest.getEventList()

        assertThat(result).hasSize(1)
        assertThat(result).contains(expected)
    }

    @Test
    fun `assert get event`() = runTest {
        val expectedEvent = event()
        val expectedId = expectedEvent.id!!

        coEvery { eventDao.getById(any()) } returns eventEntity()
        every { mapper.map(any()) } returns expectedEvent

        val result = subjectUnderTest.getEvent(expectedId)

        assertThat(result).isEqualTo(expectedEvent)
    }

    @Test
    fun `verify createEvent`(): Unit = runTest {
        val model = event().copy(id = "")
        val slot = slot<EventEntity>()

        coEvery { eventDao.upsertAll(capture(slot)) } just runs
        every { mapper.reverseMap(model) } returns eventEntity()

        val result = subjectUnderTest.createEvent(model)

        assertThat(result).isNotEmpty()
        coVerify(exactly = 1) { eventDao.upsertAll(any()) }
        assertThat(slot.captured.id).isEqualTo(result)
    }

    @Test
    fun `verify update event`() = runTest {
        val model = event()
        val slot = slot<EventEntity>()
        val expected = eventEntity()

        coEvery { eventDao.upsertAll(capture(slot)) } just runs
        every { mapper.reverseMap(model) } returns expected

        subjectUnderTest.updateEvent(model)

        coVerify(exactly = 1) { eventDao.upsertAll(expected) }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `verify update event with empty id returns`() = runTest {
        val model = event().copy(id = "")

        subjectUnderTest.updateEvent(model)
    }

    @Test
    fun `verify delete event`() = runTest {
        val entityId = "event-id"

        coEvery { eventDao.deleteById(any()) } just runs

        subjectUnderTest.deleteEvent(entityId)

        coVerify(exactly = 1) { eventDao.deleteById(entityId) }
    }
}