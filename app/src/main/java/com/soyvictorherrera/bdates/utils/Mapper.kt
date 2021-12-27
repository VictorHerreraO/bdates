package com.soyvictorherrera.bdates.utils

abstract class Mapper<T, M> {

    abstract fun toModel(value: T): M

    abstract fun fromModel(model: M): T

    fun toModelList(values: List<T>): List<M> {
        return values.map {
            toModel(it)
        }
    }

    fun fromModelList(models: List<M>): List<T> {
        return models.map {
            fromModel(it)
        }
    }
}