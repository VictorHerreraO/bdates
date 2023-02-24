package com.soyvictorherrera.bdates.core.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Bossanova,
    primaryVariant = Dolphin,
    onPrimary = White,
    secondary = Paradiso,
    secondaryVariant = Tradewind,
    onSecondary = White
)

private val LightColorPalette = lightColors(
    primary = Bossanova,
    primaryVariant = Dolphin,
    onPrimary = White,
    secondary = Tradewind,
    secondaryVariant = Tradewind,
    onSecondary = White,
    surface = Alabaster,

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun BdatesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}