package com.soyvictorherrera.bdates.modules.eventList.domain.usecase

import com.soyvictorherrera.bdates.core.arch.UseCase
import com.soyvictorherrera.bdates.modules.circles.data.preferences.CirclePreferencesContract
import com.soyvictorherrera.bdates.modules.circles.data.repository.CircleRepositoryContract
import com.soyvictorherrera.bdates.modules.circles.domain.model.Circle
import com.soyvictorherrera.bdates.modules.eventList.data.repository.EventRepositoryContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import javax.inject.Inject
import timber.log.Timber

interface UpsertEventUseCaseContract : UseCase<Event, Unit>

class UpsertEventUseCase @Inject constructor(
    private val eventRepository: EventRepositoryContract,
    private val circleRepository: CircleRepositoryContract,
    private val circlePreferences: CirclePreferencesContract,
) : UpsertEventUseCaseContract {

    override suspend fun execute(params: Event) {
        val localCircleId = circlePreferences.localCircleId
            ?: circleRepository.getCircles().find { it.isDefaultCircle }?.id
            ?: circleRepository.createCircle(
                Circle(
                    id = null,
                    name = "Device local circle",
                    description = null,
                    isDefaultCircle = true
                )
            ).also { circleId ->
                circlePreferences.localCircleId = circleId
            }

        if (localCircleId == null) {
            val exception = RuntimeException("Unable to resolve local circle ID")
            Timber.e(exception)
            throw exception
        }

        // Save the resolved ID for future use
        circlePreferences.localCircleId = localCircleId

        val eventToSave = params.copy(
            circleId = params.circleId.takeIf { it.isNotEmpty() } ?: localCircleId
        )

        eventRepository.runCatching {
            if (eventToSave.id.isNullOrEmpty()) {
                createEvent(eventToSave)
            } else {
                updateEvent(eventToSave)
            }
        }.onFailure {
            Timber.e(it, "Unable to save event")
            throw it
        }
    }
}
