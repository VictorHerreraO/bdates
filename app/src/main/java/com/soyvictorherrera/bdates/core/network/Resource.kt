package com.soyvictorherrera.bdates.core.network

sealed class Resource<T>(open val data: T?) {
    class Error<T>(data: T? = null, val cause: Throwable? = null) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
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

fun <T> Resource<T>.onSuccess(action: (data: T) -> Unit): Resource<T> {
    if (this is Resource.Success) {
        action(data)
    }
    return this
}

fun <T> Resource<T>.onLoading(action: (data: T?) -> Unit): Resource<T> {
    if (this is Resource.Loading) {
        action(data)
    }
    return this
}

fun <T> Resource<T>.onError(action: (data: T?, cause: Throwable?) -> Unit): Resource<T> {
    if (this is Resource.Error) {
        action(data, cause)
    }
    return this
}

fun <T> Resource<T>.onError(action: (cause: Throwable?) -> Unit): Resource<T> {
    if (this is Resource.Error) {
        action(cause)
    }
    return this
}
