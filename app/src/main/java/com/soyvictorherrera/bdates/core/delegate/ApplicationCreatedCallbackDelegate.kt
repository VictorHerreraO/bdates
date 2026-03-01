package com.soyvictorherrera.bdates.core.delegate

import com.soyvictorherrera.bdates.core.coroutines.MainCoroutineScope
import com.soyvictorherrera.bdates.modules.appinfo.domain.AppInfoProvider
import com.soyvictorherrera.bdates.modules.notifications.NotificationManagerContract
import com.soyvictorherrera.bdates.modules.appConfig.AppConfigContract
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

interface ApplicationCreatedCallbackDelegateContract {
    fun onApplicationCreated()
}

class ApplicationCreatedCallbackDelegate @Inject constructor(
    @MainCoroutineScope private val coroutineScope: CoroutineScope,
    private val appInfoProvider: AppInfoProvider,
    private val notificationManager: NotificationManagerContract,
    private val appConfig: AppConfigContract,
) : ApplicationCreatedCallbackDelegateContract {

    override fun onApplicationCreated() {
        setupLogging()
        setupNotifications()
    }

    private fun setupLogging() {
        if (appConfig.isDebug) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun setupNotifications() {
        notificationManager.setupDayEventsReminder()
    }
}
