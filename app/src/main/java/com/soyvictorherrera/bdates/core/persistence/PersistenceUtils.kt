package com.soyvictorherrera.bdates.core.persistence

import java.util.UUID

fun randomUUID(): String = UUID.randomUUID().toString()

/**
 * Adds to [this] list the elements returned by [fetch]
 *
 * Example:
 *
 * ```
 * return mutableListOf<Event>()
 *      .addSource(localDataSource) { getLocalData() }
 *      .addSource(remoteDataSource) {
 *          getRemoteData().map {
 *              remoteMapper.map(it)
 *          }
 *      }
 * ```
 */
suspend fun <T, R> MutableList<R>.addSource(
    source: T,
    fetch: suspend T.() -> Collection<R>,
): MutableList<R> {
    addAll(source.fetch())
    return this
}
