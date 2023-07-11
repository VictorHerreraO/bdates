package com.soyvictorherrera.bdates.core.network

sealed class Resource<T>(open val data: T?) {
    class Error<T>(data: T?, val cause: Throwable? = null) : Resource<T>(data)
    class Loading<T>(data: T?) : Resource<T>(data)
    class Success<T>(override val data: T) : Resource<T>(data)
}

fun <T> T?.asError(cause: Throwable? = null): Resource.Error<T> {
    return Resource.Error(this, cause)
}

fun <T> T?.asLoading(): Resource.Loading<T> {
    return Resource.Loading(this)
}

fun <T> T.asSuccess(): Resource.Success<T> {
    return Resource.Success(this)
}
