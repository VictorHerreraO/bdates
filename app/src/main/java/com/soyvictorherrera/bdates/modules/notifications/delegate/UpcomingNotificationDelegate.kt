package com.soyvictorherrera.bdates.modules.notifications.delegate

import com.soyvictorherrera.bdates.core.date.DateProviderContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.GetUpcomingEventListUseCaseContract
import com.soyvictorherrera.bdates.modules.notifications.NotificationManagerContract
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import timber.log.Timber

interface UpcomingEventNotificationDelegateContract {
    /**
     * Show a notification for events happening in a week from now
     */
    fun notifyUpcomingEvents()
}

class UpcomingEventNotificationDelegate @Inject constructor(
    private val notificationManager: NotificationManagerContract,
    private val getUpcomingEventList: GetUpcomingEventListUseCaseContract,
    private val dateProvider: DateProviderContract,
) : UpcomingEventNotificationDelegateContract {
    override fun notifyUpcomingEvents() {
        val upcomingEvents: List<Event> = runBlocking {
            getUpcomingEventList.execute()
        }

        if (upcomingEvents.isEmpty()) {
            Timber.d("No upcoming events in a week from now")
            return
        }

        val event = upcomingEvents.first()

        notificationManager.showUpcomingEventsReminder(
            eventCount = upcomingEvents.size,
            eventName = event.name,
            eventDateString = event.nextOccurrence?.run(dateProvider::formatDateAsDay) ?: ""
        )
    }
}