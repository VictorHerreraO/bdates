package com.soyvictorherrera.bdates.modules.notifications.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.soyvictorherrera.bdates.modules.notifications.NotificationManagerContract
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

const val ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED"

@AndroidEntryPoint
class AlarmBootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationManager: NotificationManagerContract

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ACTION_BOOT_COMPLETED) {
            Timber.d("Device booted, re-schedule alarm")
            notificationManager.setupDayEventsReminder()
        }
    }
}
