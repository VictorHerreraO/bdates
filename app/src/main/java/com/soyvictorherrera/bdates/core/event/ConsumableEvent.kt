package com.soyvictorherrera.bdates.core.event

/**
 * Represents a [Consumable] that can hold a [value]. For leveraging the consumable capabilities of this
 * class use [consumeValue]. In the case the same [value] represents different consumable events an [id]
 * can be provided for distinguishing between instances.
 */
data class ConsumableEvent<T>(
    val value: T,
    private val id: Any? = null,
) : Consumable()

/**
 * Executes the provided [consumer] function only if the [ConsumableEvent] hasn't been consumed yet.
 * The provided consumer receives [ConsumableEvent.value] as an argument
 */
inline fun <T> ConsumableEvent<T>?.consumeValue(consumer: (T) -> Unit) {
    this?.consume { consumer(it.value) }
}

/**
 * Wrap [this] inside a [ConsumableEvent]. Uses [System.currentTimeMillis] as an ID by default
 */
fun <T> T.asConsumable(
    id: Any? = System.currentTimeMillis(),
): ConsumableEvent<T> = ConsumableEvent(
    value = this,
    id = id
)
