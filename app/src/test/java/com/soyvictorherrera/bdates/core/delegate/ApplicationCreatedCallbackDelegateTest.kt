package com.soyvictorherrera.bdates.core.delegate

import com.google.common.truth.Truth.assertThat
import com.soyvictorherrera.bdates.modules.appinfo.domain.AppInfoProvider
import com.soyvictorherrera.bdates.modules.notifications.NotificationManagerContract
import com.soyvictorherrera.bdates.util.TimberTestRule
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import timber.log.Timber

@OptIn(ExperimentalCoroutinesApi::class)
class ApplicationCreatedCallbackDelegateTest {

    @get:Rule
    val timberRule = TimberTestRule()

    private lateinit var mockAppInfo: AppInfoProvider

    private lateinit var notificationManager: NotificationManagerContract

    private lateinit var subjectUnderTest: ApplicationCreatedCallbackDelegate

    private val testScope = TestScope()


    @Before
    fun setUp() {
        mockAppInfo = mockk {
            every { isDebugBuild } returns true
        }
        notificationManager = mockk {
            every { setupDayEventsReminder() } just runs
        }
        subjectUnderTest = ApplicationCreatedCallbackDelegate(
            coroutineScope = testScope,
            appInfoProvider = mockAppInfo,
            notificationManager = notificationManager
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
    fun `on application created skips timber setup when not in debug build`() {
        every { mockAppInfo.isDebugBuild } returns false

        assertThat(Timber.forest()).isEmpty()

        subjectUnderTest.onApplicationCreated()

        assertThat(Timber.forest()).hasSize(0)
    }

    @Test
    fun `on application created calls setup day events reminder`() {
        subjectUnderTest.onApplicationCreated()

        verify(exactly = 1) { notificationManager.setupDayEventsReminder() }
    }

}