package com.soyvictorherrera.bdates.modules.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.soyvictorherrera.bdates.core.HomeNavigationActivity
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import android.app.NotificationChannel as AndroidNotificationChannel
import android.app.NotificationManager as AndroidNotificationManager

class NotificationManager @Inject constructor(
    private val context: Context
) : NotificationManagerContract {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as AndroidNotificationManager

    init {
        // Create channels as soon as possible
        createNotificationChannels()
    }

    override fun setupDayEventsReminder() {
        val intent = Intent(context, AlarmBroadcastReceiver::class.java).apply {
            action = NotificationAction.NOTIFY_DAY_EVENTS
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val alarmTime = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 18)
            set(Calendar.MINUTE, 46)
        }
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            alarmTime.timeInMillis,
            AlarmManager.INTERVAL_HALF_HOUR,
            pendingIntent
        )
        Timber.d("Inexact repeating alarm set")
    }

    override fun showDayEventsReminder() {
        val intent = Intent(context, HomeNavigationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val builder = NotificationCompat.Builder(context, NotificationChannel.CHANNEL_DAY_EVENTS)
            .setContentTitle("🍰 There is 1 birthday happening today!")
            .setContentText("Today is {name} birthday. Make sure to say hi 👋")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        notificationManager.notify(NotificationId.ID_DAY_EVENTS, builder.build())
        Timber.d("Day events reminder notification sent")
    }

    private fun createNotificationChannels() {
        Timber.v("Creating notification channels...")
        createDayEventNotificationChannel()
    }

    private fun createDayEventNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Day Events"
            val channelDescription = "Daily reminders for day events"
            val channelImportance = AndroidNotificationManager.IMPORTANCE_DEFAULT
            val channel = AndroidNotificationChannel(
                NotificationChannel.CHANNEL_DAY_EVENTS,
                channelName,
                channelImportance
            ).apply {
                description = channelDescription
            }
            notificationManager.createNotificationChannel(channel)
            Timber.v("Day events notification channel created")
        }
    }
}