package com.soyvictorherrera.bdates.modules.notifications

/**
 * Handle anything related to showing or scheduling notifications
 */
interface NotificationManagerContract {
    fun setupDayEventsReminder()
    fun showDayEventsReminder()
}