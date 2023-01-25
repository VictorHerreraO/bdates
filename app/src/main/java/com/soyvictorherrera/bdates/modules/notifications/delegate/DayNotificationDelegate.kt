package com.soyvictorherrera.bdates.modules.notifications.delegate

import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.GetDayEventListUseCaseContract
import com.soyvictorherrera.bdates.modules.notifications.NotificationManagerContract
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import timber.log.Timber

interface DayNotificationDelegateContract {
    fun notifyDayEvents()
}

class DayNotificationDelegate @Inject constructor(
    private val notificationManager: NotificationManagerContract,
    private val getDayEventList: GetDayEventListUseCaseContract,
) : DayNotificationDelegateContract {
    override fun notifyDayEvents() {
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