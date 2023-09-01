package com.soyvictorherrera.bdates.modules.appConfig

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import javax.inject.Inject

interface AppConfigContract {
    val appVersionCode: Long
    val isDebug: Boolean
}

class AppConfig @Inject constructor(
    application: Application,
) : AppConfigContract {
    override val appVersionCode: Long = application
        .packageManager
        .getPackageInfoCompat(application.packageName)
        .versionName
        .asVersionCode()

    override val isDebug: Boolean = application
        .applicationInfo
        .flags
        .let { flags -> flags and ApplicationInfo.FLAG_DEBUGGABLE != 0 }
}

private fun PackageManager.getPackageInfoCompat(packageName: String, flags: Int = 0): PackageInfo =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(flags.toLong()))
    } else {
        @Suppress("DEPRECATION") getPackageInfo(packageName, flags)
    }

/**
 * Split a [String] version code in the form of `"major.minor.patch"` and return it
 * as a [Long] value
 */
fun String.asVersionCode(): Long = split(".")
    .take(3)
    .mapIndexed { index, value ->
        when (index) {
            0 -> value.toLong() * 1_000_000L
            1 -> value.toLong() * 1_000L
            else -> value.toLong()
        }
    }
    .sum()
