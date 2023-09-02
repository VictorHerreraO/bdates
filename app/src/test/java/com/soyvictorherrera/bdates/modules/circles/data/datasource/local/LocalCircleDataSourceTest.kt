package com.soyvictorherrera.bdates.modules.circles.data.datasource.local

import com.google.common.truth.Truth.assertThat
import com.soyvictorherrera.bdates.modules.circles.data.mapper.CircleEntityToModelMapperContract
import com.soyvictorherrera.bdates.test.data.circleEntity
import com.soyvictorherrera.bdates.test.data.circleModel
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
class LocalCircleDataSourceTest {
    lateinit var subjectUnderTest: LocalCircleDataSource

    private val dao: CircleDao = mockk()

    private val mapper: CircleEntityToModelMapperContract = mockk()

    @Before
    fun setup() {
        subjectUnderTest = LocalCircleDataSource(
            dao = dao,
            mapper = mapper
        )
    }

    @Test
    fun `assert get circles list`(): Unit = runTest {
        val expected = circleModel()

        coEvery { dao.getAll() } returns listOf(circleEntity())
        coEvery { mapper.map(any()) } returns expected

        val result = subjectUnderTest.getCircles()

        assertThat(result).hasSize(1)
        assertThat(result).contains(expected)
    }

    @Test
    fun `assert get circle by id`(): Unit = runTest {
        val expectedId = "expected-id"
        val expected = circleModel().copy(id = expectedId)

        coEvery { dao.getById(any()) } returns circleEntity()
        every { mapper.map(any()) } returns expected

        val result = subjectUnderTest.getCircle(expectedId)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `assert create circle generates id and upserts`(): Unit = runTest {
        val entity = circleEntity().copy(id = "")
        every { mapper.reverseMap(any()) } returns entity

        val slot = slot<CircleEntity>()
        coEvery { dao.upsertAll(capture(slot)) } just runs

        val result = subjectUnderTest.createCircle(circleModel())

        coVerify(exactly = 1) { dao.upsertAll(any()) }
        assertThat(slot.captured).isNotEqualTo(entity)
        assertThat(slot.captured.id).isNotEmpty()
        assertThat(slot.captured.id).isEqualTo(result)
    }

    @Test
    fun `assert update circle calls upsert`(): Unit = runTest {
        val entity = circleEntity()
        every { mapper.reverseMap(any()) } returns entity

        val slot = slot<CircleEntity>()
        coEvery { dao.upsertAll(capture(slot)) } just runs

        subjectUnderTest.updateCircle(circleModel())

        coVerify(exactly = 1) { dao.upsertAll(any()) }
        assertThat(slot.captured).isEqualTo(entity)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `expect IllegalArgumentException when updating circle with no id`(): Unit = runTest {
        val model = circleModel().copy(id = "")

        subjectUnderTest.updateCircle(model)
    }
}