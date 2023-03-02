package com.soyvictorherrera.bdates.core.navigation

sealed class NavigationEvent {
    class NavigateBack : NavigationEvent()

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

inline fun <T : NavigationEvent> T?.consume(consumer: (T) -> Unit) {
    this?.takeIf { consumed.not() }?.let(consumer)
}
