package com.soyvictorherrera.bdates.core.event

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

inline fun <C : Consumable> C?.consume(consumer: (C) -> Unit) {
    this?.takeIf { consumed.not() }?.let(consumer)
}
