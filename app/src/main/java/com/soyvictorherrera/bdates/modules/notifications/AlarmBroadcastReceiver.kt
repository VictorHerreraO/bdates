package com.soyvictorherrera.bdates.modules.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.GetDayEventListUseCaseContract
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class AlarmBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationManager: NotificationManagerContract

    @Inject
    lateinit var getDayEventList: GetDayEventListUseCaseContract

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == NotificationAction.NOTIFY_DAY_EVENTS) {
            Timber.d("Notify day events action intent received")
            // Here I should fetch day events and notify
            notifyDayEvents()
        }
    }

    private fun notifyDayEvents() {
        val dayEvents: List<Event> = runBlocking {
            getDayEventList.execute()
        }

        if (dayEvents.isEmpty()) {
            Timber.d("No events for today")
            return
        }

        notificationManager.showDayEventsReminder(
            eventCount = dayEvents.size,
            eventName = dayEvents.first().name
        )
    }
}