package com.soyvictorherrera.bdates.core.event

data class ConsumableEvent<T>(
    val value: T,
) : Consumable()

inline fun <T> ConsumableEvent<T>?.consumeValue(consumer: (T) -> Unit) {
    this?.consume { consumer(it.value) }
}
