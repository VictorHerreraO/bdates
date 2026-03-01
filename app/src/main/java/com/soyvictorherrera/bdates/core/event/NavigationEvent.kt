package com.soyvictorherrera.bdates.core.event

sealed class NavigationEvent : Consumable() {
    class NavigateBack : NavigationEvent()
    class AddEventBottomSheet(val eventId: String? = null) : NavigationEvent()
    class PreviewEventBottomSheet(val eventId: String): NavigationEvent()
}
