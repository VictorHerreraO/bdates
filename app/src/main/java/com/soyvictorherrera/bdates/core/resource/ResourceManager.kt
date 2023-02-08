package com.soyvictorherrera.bdates.core.resource

import android.content.Context
import androidx.annotation.StringRes

class ResourceManager(
    context: Context
) : ResourceManagerContract {

    private val resources = context.resources
    private val packageName = context.packageName

    override fun getString(identifier: String, vararg args: Any?): String {
        return try {
            resources.getIdentifier(
                identifier,
                ResourceType.STRING,
                packageName
            ).let { resId ->
                if (args.isNotEmpty()) resources.getString(resId, *args)
                else resources.getString(resId)
            }
        } catch (ex: Exception) {
            ""
        }
    }

    override fun getString(@StringRes identifier: Int, vararg args: Any?): String {
        return resources.getString(identifier, *args)
    }
}
