package com.soyvictorherrera.bdates.modules.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AlarmBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationManager: NotificationManager

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == NotificationAction.NOTIFY_DAY_EVENTS) {
            Timber.d("Notify day events action intent received")
            // Here I should fetch day events and notify
            notificationManager.showDayEventsReminder()
        }
    }
}