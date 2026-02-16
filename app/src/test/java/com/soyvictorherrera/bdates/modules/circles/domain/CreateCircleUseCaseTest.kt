package com.soyvictorherrera.bdates.modules.circles.domain

import com.soyvictorherrera.bdates.modules.circles.data.repository.CircleRepositoryContract
import com.soyvictorherrera.bdates.modules.circles.domain.model.Circle
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class CreateCircleUseCaseTest {

    private val circleRepository = mockk<CircleRepositoryContract>()

    private lateinit var subjectUnderTest: CreateCircleUseCase

    @Before
    fun setup() {
        subjectUnderTest = CreateCircleUseCase(
            circleRepository = circleRepository
        )
    }

    @Test
    fun `verify circle is created successfully`(): Unit = runTest {
        val expectedId = "expected-id"
        val circle = Circle(
            id = null,
            name = "Test Circle",
            description = "Test description",
            isDefaultCircle = false
        )

        coEvery { circleRepository.createCircle(any()) } returns expectedId

        subjectUnderTest.execute(circle)

        coVerify(exactly = 1) { circleRepository.createCircle(circle) }
    }

    @Test
    fun `verify default circle can be created`(): Unit = runTest {
        val expectedId = "device-local"
        val defaultCircle = Circle(
            id = null,
            name = "Device local circle",
            description = "",
            isDefaultCircle = true
        )

        coEvery { circleRepository.createCircle(any()) } returns expectedId

        subjectUnderTest.execute(defaultCircle)

        coVerify(exactly = 1) { circleRepository.createCircle(defaultCircle) }
    }

    @Test
    fun `verify custom circle with custom name is created`(): Unit = runTest {
        val expectedId = "family-circle-id"
        val customCircle = Circle(
            id = null,
            name = "Family",
            description = "Family members",
            isDefaultCircle = false
        )

        coEvery { circleRepository.createCircle(any()) } returns expectedId

        subjectUnderTest.execute(customCircle)

        coVerify(exactly = 1) { circleRepository.createCircle(customCircle) }
    }
}
