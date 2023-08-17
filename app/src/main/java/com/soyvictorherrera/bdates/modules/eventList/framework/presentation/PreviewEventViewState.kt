package com.soyvictorherrera.bdates.modules.eventList.framework.presentation

data class PreviewEventViewState(
    val ordinalAge: String?,
    val eventName: String,
    val eventDate: String,
    val eventType: String,
    val isEditable: Boolean,
    val circleName: String? = null,
    val isLoading: Boolean,
    val remainingDays: String,
)
