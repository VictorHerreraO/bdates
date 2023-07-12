package com.soyvictorherrera.bdates.modules.eventList.domain.usecase

import com.soyvictorherrera.bdates.core.arch.UseCase
import com.soyvictorherrera.bdates.core.date.DateProviderContract
import com.soyvictorherrera.bdates.core.date.toEpochMilli
import com.soyvictorherrera.bdates.core.network.Resource
import com.soyvictorherrera.bdates.modules.circles.data.preferences.CirclePreferencesContract
import com.soyvictorherrera.bdates.modules.circles.data.repository.CircleRepositoryContract
import com.soyvictorherrera.bdates.modules.circles.domain.model.Circle
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import javax.inject.Inject
import timber.log.Timber

interface UpdateEventsUseCaseContract : UseCase<Unit, Resource<List<Event>>>

class UpdateEventsUseCase @Inject constructor(
    private val circlesRepo: CircleRepositoryContract,
    private val circlePrefs: CirclePreferencesContract,
    private val dateProvider: DateProviderContract,
) : UpdateEventsUseCaseContract {
    override suspend fun execute(params: Unit): Resource<List<Event>> {
        fetchCircles().forEach {
            fetchCircleEvents(it)
        }
        return Resource.Success(emptyList())
    }

    private suspend fun fetchCircles(): List<Circle> {
        return when (val resource = circlesRepo.getCircles()) {
            is Resource.Success -> {
                resource.data
            }

            else -> {
                if (resource is Resource.Error) {
                    Timber.e(resource.cause)
                }
                emptyList()
            }
        }
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

        val now = dateProvider.currentLocalDateTime.toEpochMilli()
        val lastCheck = circle.lastUpdateCheck ?: 0

        Timber.d("Working with ${circle.name}")
        Timber.d("last check was: $lastCheck")
        Timber.d("Once refreshed will update with millis = $now")

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