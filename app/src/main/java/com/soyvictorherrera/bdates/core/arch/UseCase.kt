package com.soyvictorherrera.bdates.core.arch

interface UseCase<Params, ReturnValue> {
    suspend fun execute(params: Params): ReturnValue
}
