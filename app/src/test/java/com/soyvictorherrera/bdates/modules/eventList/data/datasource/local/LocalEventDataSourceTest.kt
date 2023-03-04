package com.soyvictorherrera.bdates.modules.eventList.data.datasource.local

import com.google.common.truth.Truth.assertThat
import com.soyvictorherrera.bdates.test.data.eventEntity
import io.mockk.coEvery
import io.mockk.coVerify
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

    private val eventDao = mockk<EventDao>()

    private lateinit var subjectUnderTest: LocalEventDataSource

    @Before
    fun setup() {
        subjectUnderTest = LocalEventDataSource(eventDao)
    }

    @Test
    fun `assert getGetEventList`(): Unit = runTest {
        val expectedEvent = eventEntity()

        coEvery { eventDao.getAll() } returns listOf(expectedEvent)

        val result = subjectUnderTest.getEventList()

        assertThat(result).hasSize(1)
        assertThat(result).contains(expectedEvent)
    }

    @Test
    fun `verify createEvent`(): Unit = runTest {
        val entity = eventEntity().copy(id = "")
        val slot = slot<EventEntity>()

        coEvery { eventDao.upsertAll(capture(slot)) } just runs

        val result = subjectUnderTest.createEvent(entity)

        assertThat(result).isNotEmpty()
        coVerify(exactly = 1) { eventDao.upsertAll(any()) }
        assertThat(slot.captured.id).isEqualTo(result)
    }

}