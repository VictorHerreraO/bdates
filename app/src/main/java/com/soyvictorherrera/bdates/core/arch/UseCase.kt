package com.soyvictorherrera.bdates.core.arch

interface UseCase<Params, ReturnValue> {
    suspend fun execute(params: Params): ReturnValue
}

/**
 * Execute the use case using `Unit` as the params.
 *
 * Shortcut for `UseCase.execute(Unit)`
 */
suspend fun <R> UseCase<Unit, R>.execute() = this.execute(Unit)
