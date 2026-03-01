package com.soyvictorherrera.bdates.core.arch

interface Mapper<T1, T2> {
    fun map(value: T1): T2
    fun reverseMap(value: T2): T1 = throw UnsupportedOperationException()
    fun mapCollection(values: Collection<T1>): Collection<T2> = values.map(::map)
    fun reverseMapCollection(values: Collection<T2>): Collection<T1> = values.map(::reverseMap)
}
