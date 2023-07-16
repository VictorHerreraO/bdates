package com.soyvictorherrera.bdates.core.event

sealed class NavigationEvent : Consumable() {
    class NavigateBack : NavigationEvent()
    class EventBottomSheet(val eventId: String? = null) : NavigationEvent()
}
