package com.soyvictorherrera.bdates.core.delegate

import com.google.common.truth.Truth.assertThat
import com.soyvictorherrera.bdates.modules.appConfig.AppConfigContract
import com.soyvictorherrera.bdates.modules.circles.domain.CreateLocalCircleUseCaseContract
import com.soyvictorherrera.bdates.modules.notifications.NotificationManagerContract
import com.soyvictorherrera.bdates.util.TimberTestRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import timber.log.Timber

@OptIn(ExperimentalCoroutinesApi::class)
class ApplicationCreatedCallbackDelegateTest {
    private lateinit var subjectUnderTest: ApplicationCreatedCallbackDelegate

    @get:Rule
    val timberRule = TimberTestRule()

    private lateinit var createLocalCircle: CreateLocalCircleUseCaseContract

    private lateinit var notificationManager: NotificationManagerContract

    private lateinit var appConfig: AppConfigContract


    private val testScope = TestScope()


    @Before
    fun setUp() {
        createLocalCircle = mockk {
            coEvery { execute() } just runs
        }
        notificationManager = mockk {
            every { setupDayEventsReminder() } just runs
        }
        appConfig = mockk {
            every { isDebug } returns true
        }
        subjectUnderTest = ApplicationCreatedCallbackDelegate(
            coroutineScope = testScope,
            createLocalCircle = createLocalCircle,
            notificationManager = notificationManager,
            appConfig = appConfig
        )
    }

    @Test
    fun `on application created setups timber debug tree when in debug build`() {
        assertThat(Timber.forest()).isEmpty()

        subjectUnderTest.onApplicationCreated()

        assertThat(Timber.forest()).hasSize(1)
        assertThat(Timber.forest().first()).isInstanceOf(Timber.DebugTree::class.java)
    }

    @Test
    fun `on application created skips timber debug tree setup when not in debug build`() {
        assertThat(Timber.forest()).isEmpty()

        subjectUnderTest.onApplicationCreated()

        assertThat(Timber.forest()).isEmpty()
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