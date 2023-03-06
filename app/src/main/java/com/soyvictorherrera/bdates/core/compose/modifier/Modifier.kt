package com.soyvictorherrera.bdates.core.compose.modifier

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.debugInspectorInfo

/**
 * Applies the result of [ifTrue] when [condition] is true, else applies [ifFalse]
 */
@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.conditional(
    condition: Boolean,
    ifFalse: @Composable (Modifier.() -> Modifier)? = null,
    ifTrue: @Composable Modifier.() -> Modifier,
): Modifier = composed(inspectorInfo = debugInspectorInfo {
    name = "conditional"
    value = condition
}) {
    return@composed if (condition) {
        then(ifTrue(Modifier))
    } else if (ifFalse != null) {
        then(ifFalse(Modifier))
    } else {
        this
    }
}

/**
 * Clear the current focus on tap.
 *
 * Useful to hide the keyboard when tapping outside a textField
 */
fun Modifier.clearFocusOnTap(

): Modifier = composed(inspectorInfo = debugInspectorInfo {
    name = "clearFocusOnTap"
}) {
    val localFocusManager = LocalFocusManager.current
    return@composed this.pointerInput(Unit) {
        detectTapGestures(onTap = {
            localFocusManager.clearFocus()
        })
    }
}
