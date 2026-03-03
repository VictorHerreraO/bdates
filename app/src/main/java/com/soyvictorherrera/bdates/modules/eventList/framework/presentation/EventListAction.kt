package com.soyvictorherrera.bdates.modules.eventList.framework.presentation

sealed class EventListAction {
    data object Refresh : EventListAction()
    data class ChangeQuery(val query: String) : EventListAction()
    data class EventClick(val eventId: String) : EventListAction()
    data object AddEventClick : EventListAction()
    data object OpenAppSettings : EventListAction()
    data class NotificationPermissionStateCheck(val isGranted: Boolean) : EventListAction()
    data class NotificationPermissionStateChanged(val isGranted: Boolean) : EventListAction()
}
