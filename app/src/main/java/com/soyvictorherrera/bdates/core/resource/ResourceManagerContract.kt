package com.soyvictorherrera.bdates.core.resource

import androidx.annotation.StringRes

interface ResourceManagerContract {
    fun getString(identifier: String, vararg args: Any?): String

    fun getString(@StringRes identifier: Int, vararg args: Any?): String
}
