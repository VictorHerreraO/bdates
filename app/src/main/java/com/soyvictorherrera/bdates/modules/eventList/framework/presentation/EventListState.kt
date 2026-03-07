package com.soyvictorherrera.bdates.modules.eventList.framework.presentation

import com.soyvictorherrera.bdates.core.event.NavigationEvent

data class EventListState(
    val events: List<EventViewState> = emptyList(),
    val todayEvents: List<TodayEventViewState> = emptyList(),
    val isRefreshing: Boolean = false,
    val query: String = "",
    val showMissingPermissionMessage: Boolean = false,
    val requestPermission: Boolean = true,
    val errorMessage: String? = null,
    val navigationEvent: NavigationEvent? = null,
) {
    val showEmptyState: Boolean
        get() = events.isEmpty() && query.isEmpty()

    val showTodayEvents: Boolean
        get() = todayEvents.isNotEmpty()
}
