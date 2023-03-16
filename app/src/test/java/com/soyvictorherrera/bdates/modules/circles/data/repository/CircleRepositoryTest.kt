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
}