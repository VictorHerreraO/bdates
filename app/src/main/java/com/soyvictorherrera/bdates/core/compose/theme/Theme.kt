package com.soyvictorherrera.bdates.core.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.MaterialTheme as Material3Theme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

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
    secondary = Tradewind,
    secondaryVariant = Tradewind,
    background = Bossanova,
    surface = White,
    error = Color(0xFFB00020),
    onPrimary = White,
    onSecondary = White,
    onBackground = White,
    onSurface = Black,
    onError = White
)

private val LightColorScheme = lightColorScheme(
    primary = Bossanova,
    onPrimary = White,
    secondary = Tradewind,
    onSecondary = White,
    background = Bossanova,
    surface = White,
    onBackground = White,
    onSurface = Black,
    tertiary = Bossanova,
    surfaceVariant = Gallery,
    onSurfaceVariant = Black
)

private val DarkColorScheme = darkColorScheme(
    primary = Bossanova,
    onPrimary = White,
    secondary = Tradewind,
    onSecondary = White,
    background = Color(0xFF121212),
    surface = Cod_Gray,
    onBackground = White,
    onSurface = White,
    tertiary = White,
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = White
)

@Composable
fun BdatesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes
    ) {
        Material3Theme(
            colorScheme = colorScheme,
            content = content
        )
    }
}