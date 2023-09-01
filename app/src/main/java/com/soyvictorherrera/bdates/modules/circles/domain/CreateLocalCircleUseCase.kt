package com.soyvictorherrera.bdates.modules.circles.domain

import com.soyvictorherrera.bdates.core.arch.UseCase
import com.soyvictorherrera.bdates.modules.circles.data.preferences.CirclePreferencesContract
import com.soyvictorherrera.bdates.modules.circles.data.repository.CircleRepositoryContract
import com.soyvictorherrera.bdates.modules.circles.domain.model.Circle
import javax.inject.Inject
import timber.log.Timber

interface CreateLocalCircleUseCaseContract : UseCase<Unit, Unit> {
    suspend fun execute()

    override suspend fun execute(params: Unit) = execute()
}

class CreateLocalCircleUseCase @Inject constructor(
    private val circlePreferences: CirclePreferencesContract,
    private val circleRepository: CircleRepositoryContract,
) : CreateLocalCircleUseCaseContract {

    private companion object {
        val LOCAL_CIRCLE = Circle(
            id = null,
            name = "Device local circle",
            description = "",
            isLocalOnly = true,
            updateDate = null,
        )
    }

    override suspend fun execute() {
        if (circlePreferences.isLocalCircleCreated) {
            Timber.v("Local circle already exists")
            return
        }

        circleRepository.runCatching {
            createCircle(LOCAL_CIRCLE)
        }.onSuccess { localCircleId ->
            circlePreferences.isLocalCircleCreated = true
            circlePreferences.localCircleId = localCircleId
            Timber.d("Local circle created with id $localCircleId")
        }
    }
}
