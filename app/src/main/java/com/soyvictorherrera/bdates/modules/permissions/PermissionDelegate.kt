package com.soyvictorherrera.bdates.modules.permissions

import android.Manifest
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity

typealias PermissionRequestCallback = (isGranted: Boolean) -> Unit

class PermissionDelegate(
    private val activity: FragmentActivity,
) {

    private val launcher = activity.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        callback?.invoke(isGranted)
        callback = null
    }

    private var callback: PermissionRequestCallback? = null

    fun requestNotificationPermission(callback: PermissionRequestCallback) {
        if (activity.isPostNotificationPermissionGranted) {
            callback(true)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            this.callback = callback
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

object PermissionDelegateFactory {
    fun create(
        activityProducer: () -> FragmentActivity
    ): PermissionDelegate {
        return PermissionDelegate(
            activity = activityProducer()
        )
    }
}
