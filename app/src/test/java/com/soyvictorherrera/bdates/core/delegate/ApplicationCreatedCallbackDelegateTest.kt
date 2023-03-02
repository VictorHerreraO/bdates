package com.soyvictorherrera.bdates.core.delegate

import com.soyvictorherrera.bdates.modules.circles.domain.CreateLocalCircleUseCaseContract
import com.soyvictorherrera.bdates.modules.notifications.NotificationManagerContract
import io.mockk.coEvery
import io.mockk.just
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ApplicationCreatedCallbackDelegateTest {

    private lateinit var createLocalCircle: CreateLocalCircleUseCaseContract

    private lateinit var notificationManager: NotificationManagerContract

    private lateinit var subjectUnderTest: ApplicationCreatedCallbackDelegate

    private val testScope = TestScope()


    @Before
    fun setUp() {
        createLocalCircle = mockk {
            coEvery { execute() } just runs
        }
        notificationManager = mockk {
            every { setupDayEventsReminder() } just runs
        }
        subjectUnderTest = ApplicationCreatedCallbackDelegate(
            coroutineScope = testScope,
            createLocalCircle = createLocalCircle,
            notificationManager = notificationManager
        )
    }

    @Test
    fun `on application created calls create local circle`() {
        subjectUnderTest.onApplicationCreated()

        testScope.launch {
            coVerify(exactly = 1) { createLocalCircle.execute() }
        }
    }

    @Test
    fun `on application created calls setup day events reminder`() {
        subjectUnderTest.onApplicationCreated()

        verify(exactly = 1) { notificationManager.setupDayEventsReminder() }
    }

}