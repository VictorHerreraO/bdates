package com.soyvictorherrera.bdates.modules.notifications

/**
 * Handle anything related to showing or scheduling notifications
 */
interface NotificationManagerContract {
    /**
     * Configures an alarm to go off daily at 8am
     */
    fun setupDayEventsReminder()

    /**
     * Display a notification for day events
     */
    fun showDayEventsReminder(eventCount: Int, eventName: String)

    /**
     * Display a notification for upcoming events
     */
    fun showUpcomingEventsReminder(eventCount: Int, eventName: String, eventDateString: String)
}