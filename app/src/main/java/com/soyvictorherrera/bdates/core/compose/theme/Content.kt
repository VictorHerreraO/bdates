package com.soyvictorherrera.bdates.core.compose.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.ComposeView

fun ComposeView.setBdatesContent(content: @Composable () -> Unit) {
    setContent {
        val size: Size = getSizes()
        CompositionLocalProvider(LocalSizes provides size) {
            BdatesTheme {
                content()
            }
        }
    }
}