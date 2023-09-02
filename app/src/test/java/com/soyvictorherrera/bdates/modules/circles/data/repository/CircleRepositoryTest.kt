package com.soyvictorherrera.bdates.modules.circles.data.repository

import com.google.common.truth.Truth.assertThat
import com.soyvictorherrera.bdates.core.network.Resource
import com.soyvictorherrera.bdates.modules.circles.data.datasource.local.LocalCircleDataSourceContract
import com.soyvictorherrera.bdates.modules.circles.data.datasource.remote.RemoteCircleDataSourceContract
import com.soyvictorherrera.bdates.test.data.circleModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CircleRepositoryTest {
    private lateinit var subjectUnderTest: CircleRepository

    private val localDataSource: LocalCircleDataSourceContract = mockk()

    private val remoteDataSource: RemoteCircleDataSourceContract = mockk()

    @Before
    fun setUp() {
        subjectUnderTest = CircleRepository(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource
        )
    }

    @Test
    fun `assert get circles updates local circles with remote ones`(): Unit = runTest {
        val expected = circleModel()

        coEvery { remoteDataSource.getCircles() } returns listOf(expected)
        coEvery { localDataSource.getCircles() } returns listOf(expected)

        val result = subjectUnderTest.getCircles()

        coVerify(exactly = 1) { localDataSource.updateCircle(expected) }

        assertThat(result is Resource.Success).isTrue()
        assertThat(result.data).isNotEmpty()
        assertThat(result.data).contains(expected)
    }

    @Test
    fun `assert get circle`(): Unit = runTest {
        val expectedId = "expected-id"
        val expected = circleModel().copy(id = expectedId)

        coEvery { localDataSource.getCircle(expectedId) } returns expected

        val result = subjectUnderTest.getCircle(expectedId)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `verify create circle`(): Unit = runTest {
        val expectedId = "expected-id"
        val model = circleModel().copy(id = "")

        coEvery { localDataSource.createCircle(model) } returns expectedId

        val result = subjectUnderTest.createCircle(model)

        assertThat(result).isEqualTo(expectedId)
        coVerify(exactly = 1) { localDataSource.createCircle(model) }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `expect IllegalArgumentException when creating circle with id`(): Unit = runTest {
        val model = circleModel().copy(id = "circle-id")

        subjectUnderTest.createCircle(model)
    }

    @Test
    fun `verify update circle`(): Unit = runTest {
        val model = circleModel()

        coEvery { localDataSource.updateCircle(model) } just runs

        subjectUnderTest.updateCircle(model)

        coVerify(exactly = 1) { localDataSource.updateCircle(model) }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `expect IllegalArgumentException when updating circle without id`(): Unit = runTest {
        val model = circleModel().copy(id = null)

        subjectUnderTest.updateCircle(model)
    }
}