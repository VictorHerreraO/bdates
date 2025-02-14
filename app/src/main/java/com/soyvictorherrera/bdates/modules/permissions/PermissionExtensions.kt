package com.soyvictorherrera.bdates.modules.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

internal fun Context.checkPermissionGranted(
    permission: String,
): Boolean = ContextCompat
    .checkSelfPermission(this, permission)
    .let { state -> state == PackageManager.PERMISSION_GRANTED }

val Context.isPostNotificationPermissionGranted: Boolean
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.checkPermissionGranted(Manifest.permission.POST_NOTIFICATIONS)
    } else {
        true
    }
