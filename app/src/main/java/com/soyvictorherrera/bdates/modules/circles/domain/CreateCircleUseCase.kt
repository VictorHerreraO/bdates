package com.soyvictorherrera.bdates.modules.circles.domain

import com.soyvictorherrera.bdates.core.arch.UseCase
import com.soyvictorherrera.bdates.modules.circles.data.repository.CircleRepositoryContract
import com.soyvictorherrera.bdates.modules.circles.domain.model.Circle
import javax.inject.Inject
import timber.log.Timber

interface CreateCircleUseCaseContract : UseCase<Circle, Unit>

class CreateCircleUseCase @Inject constructor(
    private val circleRepository: CircleRepositoryContract,
) : CreateCircleUseCaseContract {

    override suspend fun execute(params: Circle) {
        circleRepository.runCatching {
            createCircle(params)
        }.onSuccess { circleId ->
            Timber.d("Circle '${params.name}' created with id $circleId")
        }
    }
}
