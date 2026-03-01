package com.soyvictorherrera.bdates.modules.appUpdate.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.soyvictorherrera.bdates.modules.appConfig.AppConfigContract
import com.soyvictorherrera.bdates.modules.appUpdate.data.preferences.UpdatePreferencesContract
import com.soyvictorherrera.bdates.modules.appUpdate.domain.AppVersionCodes
import com.soyvictorherrera.bdates.modules.appUpdate.domain.MigrateAppFromVersionCodeUseCaseContract
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@AndroidEntryPoint
class PackageReplacedReceiver : BroadcastReceiver() {

    @Inject
    lateinit var updatePreferences: UpdatePreferencesContract

    @Inject
    lateinit var appConfig: AppConfigContract

    @Inject
    lateinit var migrateAppFromVersionCode: MigrateAppFromVersionCodeUseCaseContract

    override fun onReceive(context: Context?, intent: Intent?) = goAsync {
        if (intent?.action != Intent.ACTION_MY_PACKAGE_REPLACED) {
            return@goAsync
        }
        Timber.d("app was updated!")

        val previousVersion = updatePreferences.previousVersionCode ?: AppVersionCodes.CODE_1_0_0
        migrateAppFromVersionCode.execute(previousVersion)

        updatePreferences.previousVersionCode = appConfig.appVersionCode
    }
}

private fun BroadcastReceiver.goAsync(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> Unit,
) {
    val pendingResult = goAsync()
    CoroutineScope(SupervisorJob()).launch(context) {
        try {
            block()
        } finally {
            pendingResult.finish()
        }
    }
}
