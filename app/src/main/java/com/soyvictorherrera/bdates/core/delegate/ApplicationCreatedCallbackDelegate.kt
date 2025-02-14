package com.soyvictorherrera.bdates.core.delegate

import com.soyvictorherrera.bdates.BuildConfig
import com.soyvictorherrera.bdates.core.coroutines.MainCoroutineScope
import com.soyvictorherrera.bdates.modules.circles.domain.CreateLocalCircleUseCaseContract
import com.soyvictorherrera.bdates.modules.notifications.NotificationManagerContract
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

interface ApplicationCreatedCallbackDelegateContract {
    fun onApplicationCreated()
}

class ApplicationCreatedCallbackDelegate @Inject constructor(
    @MainCoroutineScope private val coroutineScope: CoroutineScope,
    private val createLocalCircle: CreateLocalCircleUseCaseContract,
    private val notificationManager: NotificationManagerContract,
) : ApplicationCreatedCallbackDelegateContract {

    override fun onApplicationCreated() {
        setupLogging()
        setupLocalCircle()
        setupNotifications()
    }

    private fun setupLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun setupLocalCircle() {
        coroutineScope.launch {
            createLocalCircle.execute()
        }
    }

    private fun setupNotifications() {
        notificationManager.setupDayEventsReminder()
    }
}
