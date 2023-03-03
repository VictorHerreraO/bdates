package com.soyvictorherrera.bdates.modules.circles.domain

import com.soyvictorherrera.bdates.modules.circles.data.preferences.CirclePreferencesContract
import com.soyvictorherrera.bdates.modules.circles.data.repository.CircleRepositoryContract
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class CreateLocalCircleUseCaseTest {

    private val circlePreferences = mockk<CirclePreferencesContract>(relaxed = true)
    private val circleRepository = mockk<CircleRepositoryContract>()

    private lateinit var subjectUnderTest: CreateLocalCircleUseCase

    @Before
    fun setup() {
        subjectUnderTest = CreateLocalCircleUseCase(
            circlePreferences = circlePreferences,
            circleRepository = circleRepository
        )
    }

    @Test
    fun `verify createCircle is created and id is stored`(): Unit = runTest {
        val expectedId = "expected-id"

        every { circlePreferences.isLocalCircleCreated } returns false
        coEvery { circleRepository.createCircle(any(), any()) } answers {
            secondArg<(String) -> Unit>().invoke(expectedId)
        }

        subjectUnderTest.execute()

        coVerify(exactly = 1) { circleRepository.createCircle(any(), any()) }
        verify(exactly = 1) { circlePreferences.isLocalCircleCreated = true }
        verify(exactly = 1) { circlePreferences.localCircleId = any() }
    }

    @Test
    fun `verify createCircle is not called when localId is not null`() = runTest {
        every { circlePreferences.isLocalCircleCreated } returns true

        subjectUnderTest.execute()

        coVerify(exactly = 0) { circleRepository.createCircle(any(), any()) }
    }
}
