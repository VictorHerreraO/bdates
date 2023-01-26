package com.soyvictorherrera.bdates.modules.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import com.soyvictorherrera.bdates.R
import com.soyvictorherrera.bdates.core.HomeNavigationActivity
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import android.app.NotificationChannel as AndroidNotificationChannel
import android.app.NotificationManager as AndroidNotificationManager

private const val DEFAULT_HOUR_OF_DAY = 8
private const val DEFAULT_MINUTE_OF_HOUR = 0

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
            action = NotificationAction.NOTIFY_EVENTS
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val alarmTime = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, DEFAULT_HOUR_OF_DAY)
            set(Calendar.MINUTE, DEFAULT_MINUTE_OF_HOUR)
        }
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            alarmTime.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Timber.d("Inexact repeating alarm set")
    }

    override fun showDayEventsReminder(eventCount: Int, eventName: String) {
        if (eventCount <= 0) {
            Timber.v("eventCount is 0")
            return
        }
        val pendingIntent = buildOpenAppPendingIntent()
        val title = context.resources.getQuantityString(
            R.plurals.notification_title_day_event,
            eventCount,
            eventCount
        )
        val content = context.resources.getQuantityString(
            R.plurals.notification_content_day_event,
            eventCount,
            eventName,
            eventCount.dec()
        )
        Timber.d("Showing Day events reminder notification")
        showNotification(
            smallIcon = R.drawable.ic_celebration_48,
            notificationId = NotificationId.ID_DAY_EVENTS,
            channelId = NotificationChannel.CHANNEL_EVENT_REMINDERS,
            title = title,
            content = content,
            pendingIntent = pendingIntent
        )
    }

    override fun showUpcomingEventsReminder(
        eventCount: Int,
        eventName: String,
        eventDateString: String
    ) {
        if (eventCount <= 0) {
            Timber.v("event count is 0")
        }
        val pendingIntent = buildOpenAppPendingIntent()
        val title = context.resources.getQuantityString(
            R.plurals.notification_title_upcoming_event,
            eventCount,
            eventCount
        )
        val args: Array<Any> = arrayListOf<Any>().apply {
            add(eventName)
            val others = eventCount.dec()
            if (others > 0) {
                add(others)
            }
            add(eventDateString)
        }.toArray()
        val content = context.resources.getQuantityString(
            R.plurals.notification_content_upcoming_event,
            eventCount,
            *args
        )
        Timber.d("Showing Upcoming event reminder notification")
        showNotification(
            smallIcon = R.drawable.ic_event_48,
            notificationId = NotificationId.ID_UPCOMING_EVENTS,
            channelId = NotificationChannel.CHANNEL_EVENT_REMINDERS,
            title = title,
            content = content,
            pendingIntent = pendingIntent
        )
    }

    private fun showNotification(
        notificationId: Int,
        channelId: String,
        title: String,
        content: String? = null,
        pendingIntent: PendingIntent? = null,
        autoCancel: Boolean = true,
        @DrawableRes smallIcon: Int = R.drawable.ic_launcher_foreground,
        priority: Int = NotificationCompat.PRIORITY_DEFAULT,
    ) {
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(smallIcon)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(priority)
            .setContentIntent(pendingIntent)
            .setAutoCancel(autoCancel)

        notificationManager.notify(
            notificationId, builder.build()
        )
        Timber.v("Notification pushed to $channelId")
    }

    private fun createNotificationChannels() {
        Timber.v("Creating notification channels...")
        // Event reminders notification channel
        createNotificationChannel(
            channelId = NotificationChannel.CHANNEL_EVENT_REMINDERS,
            channelName = context.getString(R.string.notification_channel_name_event_reminders),
            channelDescription = context.getString(R.string.notification_channel_description_event_reminders)
        )
    }

    private fun createNotificationChannel(
        channelId: String,
        channelName: String,
        channelDescription: String? = null,
        channelImportance: Int = AndroidNotificationManager.IMPORTANCE_DEFAULT
    ) {
        val channel = AndroidNotificationChannel(
            channelId,
            channelName,
            channelImportance
        ).apply {
            description = channelDescription
        }
        notificationManager.createNotificationChannel(channel)
        Timber.v("$channelName notification channel created")
    }

    private fun buildOpenAppPendingIntent(): PendingIntent {
        val intent = Intent(context, HomeNavigationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }
}