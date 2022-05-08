package com.soyvictorherrera.bdates.core.arch

abstract class Mapper<T1, T2> {
    abstract fun map(value: T1): T2
    open fun reverseMap(value: T2): T1 = throw UnsupportedOperationException()
    fun mapCollection(values: Collection<T1>): Collection<T2> = values.map(::map)
    fun reverseMapCollection(values: Collection<T2>): Collection<T1> = values.map(::reverseMap)
}
