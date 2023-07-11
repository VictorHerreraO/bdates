package com.soyvictorherrera.bdates.modules.eventList.domain.usecase

import com.soyvictorherrera.bdates.core.arch.UseCase
import com.soyvictorherrera.bdates.core.network.Resource
import com.soyvictorherrera.bdates.modules.circles.data.repository.CircleRepositoryContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import javax.inject.Inject
import timber.log.Timber

interface UpdateEventsUseCaseContract : UseCase<Unit, Resource<List<Event>>>

class UpdateEventsUseCase @Inject constructor(
    private val circlesRepo: CircleRepositoryContract,
) : UpdateEventsUseCaseContract {
    override suspend fun execute(params: Unit): Resource<List<Event>> {
        when (val resource = circlesRepo.getCircles()) {
            is Resource.Error -> {
                Timber.e(resource.cause, "error while fetching the circles")
                Timber.d("Local circles are:")
                resource.data?.forEach { Timber.d("circle: $it") }
            }

            is Resource.Loading -> {
                // no-op
            }

            is Resource.Success -> {
                Timber.d("Updated circles:")
                resource.data.forEach { Timber.d("circle: $it") }
            }
        }
        return Resource.Success(emptyList())
    }
}