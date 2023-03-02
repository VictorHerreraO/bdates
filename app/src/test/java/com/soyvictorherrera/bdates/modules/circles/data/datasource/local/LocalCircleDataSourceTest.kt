package com.soyvictorherrera.bdates.modules.circles.data.datasource.local

import com.google.common.truth.Truth.assertThat
import com.soyvictorherrera.bdates.modules.circles.data.circleEntity
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
class LocalCircleDataSourceTest {

    private val dao = mockk<CircleDao>()

    lateinit var subjectUnderTest: LocalCircleDataSource

    @Before
    fun setup() {
        subjectUnderTest = LocalCircleDataSource(dao)
    }

    @Test
    fun `assert get circles list`(): Unit = runTest {
        val expected = circleEntity()

        coEvery { dao.getAll() } returns listOf(expected)

        val result = subjectUnderTest.getCircles()

        assertThat(result).hasSize(1)
        assertThat(result).contains(expected)
    }

    @Test
    fun `assert get circle by id`(): Unit = runTest {
        val expectedId = "expected-id"
        val expected = circleEntity().copy(id = expectedId)

        coEvery { dao.getById(any()) } returns expected

        val result = subjectUnderTest.getCircle(expectedId)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `assert create circle generates id and upserts`(): Unit = runTest {
        val entity = circleEntity().copy(id = "")

        val slot = slot<CircleEntity>()
        coEvery { dao.upsertAll(capture(slot)) } just runs

        val result = subjectUnderTest.createCircle(entity)

        coVerify(exactly = 1) { dao.upsertAll(any()) }
        assertThat(slot.captured).isNotEqualTo(entity)
        assertThat(slot.captured.id).isNotEmpty()
        assertThat(slot.captured.id).isEqualTo(result)
    }

    @Test
    fun `assert update circle calls upsert`(): Unit = runTest {
        val entity = circleEntity()

        val slot = slot<CircleEntity>()
        coEvery { dao.upsertAll(capture(slot)) } just runs

        subjectUnderTest.updateCircle(entity)

        coVerify(exactly = 1) { dao.upsertAll(any()) }
        assertThat(slot.captured).isEqualTo(entity)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `expect IllegalArgumentException when updating circle with no id`(): Unit = runTest {
        val entity = circleEntity().copy(id = "")

        subjectUnderTest.updateCircle(entity)
    }
}