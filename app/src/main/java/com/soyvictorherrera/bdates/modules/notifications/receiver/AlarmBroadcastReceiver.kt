package com.soyvictorherrera.bdates.modules.notifications.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.soyvictorherrera.bdates.modules.notifications.NotificationAction
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

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != NotificationAction.NOTIFY_EVENTS) {
            return
        }
        Timber.d("Notify day events action intent received")
        // Here I should fetch day events and notify
        dayNotificationDelegate.notifyDayEvents()

        upcomingEventNotificationDelegate.notifyUpcomingEvents()
    }
}