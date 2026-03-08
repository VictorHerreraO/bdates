package com.soyvictorherrera.bdates.core.event

sealed class NavigationEvent {
    data object NavigateBack : NavigationEvent()
    data class AddEventBottomSheet(val eventId: String? = null) : NavigationEvent()
    data class PreviewEventBottomSheet(val eventId: String) : NavigationEvent()
}
