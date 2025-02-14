package com.soyvictorherrera.bdates.core.delegate

import com.soyvictorherrera.bdates.core.coroutines.MainCoroutineScope
import com.soyvictorherrera.bdates.modules.appinfo.domain.AppInfoProvider
import com.soyvictorherrera.bdates.modules.circles.domain.CreateLocalCircleUseCaseContract
import com.soyvictorherrera.bdates.modules.notifications.NotificationManagerContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

interface ApplicationCreatedCallbackDelegateContract {
    fun onApplicationCreated()
}

class ApplicationCreatedCallbackDelegate @Inject constructor(
    @MainCoroutineScope private val coroutineScope: CoroutineScope,
    private val appInfoProvider: AppInfoProvider,
    private val createLocalCircle: CreateLocalCircleUseCaseContract,
    private val notificationManager: NotificationManagerContract,
) : ApplicationCreatedCallbackDelegateContract {

    override fun onApplicationCreated() {
        setupLogging()
        setupLocalCircle()
        setupNotifications()
    }

    private fun setupLogging() {
        if (appInfoProvider.isDebugBuild) {
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
