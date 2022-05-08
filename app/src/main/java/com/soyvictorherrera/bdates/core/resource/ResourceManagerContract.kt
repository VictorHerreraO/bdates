package com.soyvictorherrera.bdates.core.resource

interface ResourceManagerContract {
    fun getString(identifier: String, vararg args: Any?): String
}
