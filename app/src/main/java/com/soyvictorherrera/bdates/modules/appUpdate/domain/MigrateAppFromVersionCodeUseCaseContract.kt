package com.soyvictorherrera.bdates.modules.appUpdate.domain

import com.soyvictorherrera.bdates.core.arch.UseCase
import com.soyvictorherrera.bdates.modules.circles.data.preferences.CirclePreferencesContract
import javax.inject.Inject
import timber.log.Timber

interface MigrateAppFromVersionCodeUseCaseContract : UseCase<Long, Unit>

class MigrateAppFromVersionCodeUseCase @Inject constructor(
    private val circlePreferences: CirclePreferencesContract,
) : MigrateAppFromVersionCodeUseCaseContract {
    override suspend fun execute(params: Long) {
        Timber.d("Migrating from: $params")
        if (params < AppVersionCodes.CODE_2_0_0) {
            migrate_to_v2_0_0()
        }
    }

    private fun migrate_to_v2_0_0() {
        Timber.d("Setting default circle id")
        circlePreferences.defaultRemoteCircleId = "-NXpd9eKytV2M3bNT_m3"
    }
}