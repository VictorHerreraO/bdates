package com.soyvictorherrera.bdates.modules.eventList.framework.presentation

data class EventListState(
    val events: List<EventViewState> = emptyList(),
    val todayEvents: List<TodayEventViewState> = emptyList(),
    val isRefreshing: Boolean = false,
    val query: String = "",
    val showMissingPermissionMessage: Boolean = false,
    val requestPermission: Boolean = true,
    val errorMessage: String? = null,
) {
    val showEmptyState: Boolean
        get() = events.isEmpty() && query.isEmpty()

    val showTodayEvents: Boolean
        get() = todayEvents.isNotEmpty()
}
