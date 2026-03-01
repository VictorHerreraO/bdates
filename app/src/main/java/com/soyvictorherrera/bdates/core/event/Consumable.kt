package com.soyvictorherrera.bdates.core.event

/**
 * An abstract representation of an object that may only be used when it's not already consumed. For
 * leveraging the consumable capabilities of this class use [consume]
 */
abstract class Consumable {
    var consumed: Boolean = false
        get() {
            return if (field) {
                true
            } else {
                field = true
                false
            }
        }
        private set
}

/**
 * Executes the provided [consumer] function only if the [Consumable] hasn't been consumed yet.
 * The provided consumer receives [this] as an argument
 */
inline fun <C : Consumable> C?.consume(consumer: (C) -> Unit) {
    this?.takeIf { consumed.not() }?.let(consumer)
}
