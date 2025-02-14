package com.soyvictorherrera.bdates.modules.notifications.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.soyvictorherrera.bdates.core.date.DateProviderContract
import com.soyvictorherrera.bdates.core.date.isEqualOrAfter
import com.soyvictorherrera.bdates.modules.notifications.NotificationAction
import com.soyvictorherrera.bdates.modules.notifications.data.preferences.NotificationPreferencesContract
import com.soyvictorherrera.bdates.modules.notifications.delegate.DayNotificationDelegateContract
import com.soyvictorherrera.bdates.modules.notifications.delegate.UpcomingEventNotificationDelegateContract
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import timber.log.Timber

@AndroidEntryPoint
class AlarmBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var dayNotificationDelegate: DayNotificationDelegateContract

    @Inject
    lateinit var upcomingEventNotificationDelegate: UpcomingEventNotificationDelegateContract

    @Inject
    lateinit var notificationPreferences: NotificationPreferencesContract

    @Inject
    lateinit var dateProvider: DateProviderContract

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != NotificationAction.NOTIFY_EVENTS) {
            return
        }
        Timber.d("Notify day events action intent received")

        val lastExecutionDate = notificationPreferences.lastShownNotificationDate
        val today = dateProvider.currentLocalDate
        if (lastExecutionDate != null && lastExecutionDate.isEqualOrAfter(today)) {
            Timber.d("Notifications have been shown today")
            return
        }

        dayNotificationDelegate.notifyDayEvents()
        upcomingEventNotificationDelegate.notifyUpcomingEvents()

        notificationPreferences.lastShownNotificationDate = today
    }
}