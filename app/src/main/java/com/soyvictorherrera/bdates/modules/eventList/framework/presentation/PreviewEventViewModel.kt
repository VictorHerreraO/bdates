package com.soyvictorherrera.bdates.modules.eventList.framework.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soyvictorherrera.bdates.core.date.DateProviderContract
import com.soyvictorherrera.bdates.core.event.NavigationEvent
import com.soyvictorherrera.bdates.core.resource.ResourceManagerContract
import com.soyvictorherrera.bdates.modules.circles.data.preferences.CirclePreferencesContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import com.soyvictorherrera.bdates.modules.eventList.domain.model.nextOccurrenceAge
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.GetEventUseCaseContract
import com.soyvictorherrera.bdates.modules.eventList.framework.ui.AddEventBottomSheetArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PreviewEventViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    private val dateProvider: DateProviderContract,
    private val getEvent: GetEventUseCaseContract,
    private val resources: ResourceManagerContract,
    private val circlePreferences: CirclePreferencesContract,
) : ViewModel() {

    private val _navigation = MutableStateFlow<NavigationEvent?>(null)
    val navigation: StateFlow<NavigationEvent?> = _navigation

    private val eventId = AddEventBottomSheetArgs.fromSavedStateHandle(stateHandle).eventId
    private var currentEvent: Event? = null

    private val localCircleId: String
        get() = circlePreferences.localCircleId.orEmpty()

    private val _state = MutableStateFlow(
        PreviewEventViewState(
            age = "",
            eventName = "",
            eventDate = "",
            eventType = "",
            isEditable = false,
            isLoading = true
        )
    )
    val state: StateFlow<PreviewEventViewState> = _state

    init {
        eventId?.let(::loadEvent)
    }

    fun onActionClick() {
        _navigation.value = NavigationEvent.AddEventBottomSheet(
            eventId = eventId
        )
    }

    private fun loadEvent(eventId: String) {
        viewModelScope.launch {
            getEvent.execute(eventId)
                .onSuccess(::onEventLoaded)
                .onFailure {
                    Timber.e(it, "Unable to load event :(")
                }
        }
    }

    private fun onEventLoaded(event: Event) {
        currentEvent = event
        val upcomingFriendAge = event.nextOccurrenceAge?.toString()
        val eventDate = event.nextOccurrence?.let { nextOccurrence ->
            dateProvider.formatDateAsDayAndMonth(nextOccurrence)
        } ?: throw IllegalStateException("No next occurrence for event")

        _state.update {
            it.copy(
                age = upcomingFriendAge,
                eventName = event.name,
                eventDate = eventDate,
                eventType = resources.getString("preview_event_type_birthday"),
                isEditable = event.circleId == localCircleId,
                isLoading = false
            )
        }
    }
}
