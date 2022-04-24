package com.soyvictorherrera.bdates.core.arch

abstract class UseCase<Params, ReturnValue> {
    abstract operator fun invoke(params: Params): ReturnValue
}
