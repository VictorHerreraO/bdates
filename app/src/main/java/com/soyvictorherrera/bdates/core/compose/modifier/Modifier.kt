package com.soyvictorherrera.bdates.core.compose.modifier

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo

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