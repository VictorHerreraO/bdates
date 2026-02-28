package com.soyvictorherrera.bdates.modules.circles.data.repository

import com.google.common.truth.Truth.assertThat
import com.soyvictorherrera.bdates.core.arch.Mapper
import com.soyvictorherrera.bdates.modules.circles.data.datasource.local.CircleEntity
import com.soyvictorherrera.bdates.modules.circles.data.datasource.local.LocalCircleDataSourceContract
import com.soyvictorherrera.bdates.modules.circles.domain.model.Circle
import com.soyvictorherrera.bdates.test.data.circleEntity
import com.soyvictorherrera.bdates.test.data.circleModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CircleRepositoryTest {

    private val localDataSource = mockk<LocalCircleDataSourceContract>()
    private val localMapper = mockk<Mapper<CircleEntity, Circle>>()

    private lateinit var subjectUnderTest: CircleRepository

    @Before
    fun setUp() {
        subjectUnderTest = CircleRepository(localDataSource, localMapper)
    }

    @Test
    fun `assert get circles`(): Unit = runTest {
        val expected = circleModel()

        coEvery { localDataSource.getCircles() } returns listOf(circleEntity())
        every { localMapper.map(any()) } returns expected

        val result = subjectUnderTest.getCircles()

        assertThat(result).isNotEmpty()
        assertThat(result).contains(expected)
    }

    @Test
    fun `assert get circle`(): Unit = runTest {
        val expectedId = "expected-id"
        val expected = circleModel().copy(id = expectedId)

        coEvery { localDataSource.getCircle(expectedId) } returns circleEntity()
        every { localMapper.map(any()) } returns expected

        val result = subjectUnderTest.getCircle(expectedId)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `verify create circle`(): Unit = runTest {
        val expectedId = "expected-id"
        val circle = circleModel().copy(id = "")

        every { localMapper.reverseMap(any()) } returns circleEntity().copy(id = circle.id!!)
        coEvery { localDataSource.createCircle(any()) } returns expectedId

        val result = subjectUnderTest.createCircle(circle)

        assertThat(result).isEqualTo(expectedId)
        coVerify(exactly = 1) { localDataSource.createCircle(any()) }
    }

    @Test
    fun `verify creating multiple circles calls data source multiple times`(): Unit = runTest {
        val circle1 = circleModel().copy(id = "")
        val circle2 = circleModel().copy(id = "")
        val expectedId1 = "expected-id-1"
        val expectedId2 = "expected-id-2"

        every { localMapper.reverseMap(any()) } returns circleEntity()
        coEvery { localDataSource.createCircle(any()) } returnsMany listOf(expectedId1, expectedId2)

        val result1 = subjectUnderTest.createCircle(circle1)
        val result2 = subjectUnderTest.createCircle(circle2)

        assertThat(result1).isEqualTo(expectedId1)
        assertThat(result2).isEqualTo(expectedId2)
        coVerify(exactly = 2) { localDataSource.createCircle(any()) }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `expect IllegalArgumentException when creating circle with id`(): Unit = runTest {
        val circle = circleModel().copy(id = "circle-id")

        subjectUnderTest.createCircle(circle)
    }

    @Test
    fun `verify update circle`(): Unit = runTest {
        val circle = circleModel()

        every { localMapper.reverseMap(any()) } returns circleEntity()
        coEvery { localDataSource.updateCircle(any()) } just runs

        subjectUnderTest.updateCircle(circle)

        coVerify(exactly = 1) { localDataSource.updateCircle(any()) }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `expect IllegalArgumentException when updating circle without id`(): Unit = runTest {
        val circle = circleModel().copy(id = null)

        subjectUnderTest.updateCircle(circle)
    }

    @Test
    fun `verify delete circle`(): Unit = runTest {
        val id = "id"

        coEvery { localDataSource.deleteCircle(any()) } just runs

        subjectUnderTest.deleteCircle(id)

        coVerify(exactly = 1) { localDataSource.deleteCircle(id) }
    }
}