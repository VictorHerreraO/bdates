package com.soyvictorherrera.bdates.core.navigation

sealed class NavigationEvent {
    object NavigateBack : NavigationEvent()

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

inline fun NavigationEvent?.consume(consumer: (NavigationEvent) -> Unit) {
    this?.takeIf { consumed.not() }?.let(consumer)
}
