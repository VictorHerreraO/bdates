package com.soyvictorherrera.bdates.modules.eventList.domain.usecase

import com.soyvictorherrera.bdates.core.arch.UseCase
import com.soyvictorherrera.bdates.core.date.DateProviderContract
import com.soyvictorherrera.bdates.core.date.toEpochMilli
import com.soyvictorherrera.bdates.core.network.Resource
import com.soyvictorherrera.bdates.core.network.asSuccess
import com.soyvictorherrera.bdates.modules.circles.data.preferences.CirclePreferencesContract
import com.soyvictorherrera.bdates.modules.circles.data.repository.CircleRepositoryContract
import com.soyvictorherrera.bdates.modules.circles.domain.model.Circle
import com.soyvictorherrera.bdates.modules.eventList.data.repository.EventRepositoryContract
import javax.inject.Inject
import timber.log.Timber

interface UpdateEventsUseCaseContract : UseCase<Unit, Resource<Boolean>>

class UpdateEventsUseCase @Inject constructor(
    private val circlesRepo: CircleRepositoryContract,
    private val eventsRepo: EventRepositoryContract,
    private val circlePrefs: CirclePreferencesContract,
    private val dateProvider: DateProviderContract,
) : UpdateEventsUseCaseContract {
    private var isEventListUpdated = false

    override suspend fun execute(params: Unit): Resource<Boolean> {
        isEventListUpdated = false
        return when (val result = fetchCircles()) {
            is Resource.Success -> {
                result.data.forEach {
                    fetchCircleEvents(it)
                }
                isEventListUpdated.asSuccess()
            }

            else -> {
                val cause = (result as? Resource.Error)?.cause
                Resource.Error(cause = cause)
            }
        }
    }

    private suspend fun fetchCircles(): Resource<List<Circle>> {
        return circlesRepo.getCircles()
    }

    private suspend fun fetchCircleEvents(circle: Circle) {
        if (circle.isLocalOnly) {
            Timber.d("circle ${circle.name} is local only")
            return
        }
        if (circle.isUpToDate) {
            Timber.d("circle ${circle.name} is up-to date")
            return
        }

        val circleId = circle.id ?: return
        val now = dateProvider.currentLocalDateTime.toEpochMilli()
        val lastCheck = circle.lastUpdateCheck

        Timber.d("Working with ${circle.name}")
        Timber.d("last check was: $lastCheck")
        eventsRepo.getCircleEventList(
            circleId = circleId,
            lastUpdateDate = lastCheck
        )
        Timber.d("event list updated at: $now")
        isEventListUpdated = true

        circle.lastUpdateCheck = now
    }

    private var Circle.lastUpdateCheck: Long?
        get() = circlePrefs.updateTimestamps[id!!]
        set(value) {
            circlePrefs.updateTimestamps[id!!] = value
        }

    private val Circle.isUpToDate: Boolean
        get() {
            val lastCheck = lastUpdateCheck ?: 0
            val lastUpdate = updateDate ?: 0
            return lastCheck >= lastUpdate
        }
}