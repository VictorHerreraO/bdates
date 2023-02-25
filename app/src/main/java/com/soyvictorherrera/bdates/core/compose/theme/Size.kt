package com.soyvictorherrera.bdates.core.compose.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Size(
    val dimen_0: Dp = 0.dp,
    val dimen_2: Dp = 0.dp,
    val dimen_4: Dp = 0.dp,
    val dimen_8: Dp = 0.dp,
    val dimen_16: Dp = 0.dp,
    val dimen_24: Dp = 0.dp,
    val dimen_32: Dp = 0.dp,
    val dimen_40: Dp = 0.dp,
    val dimen_48: Dp = 0.dp,
    val dimen_56: Dp = 0.dp,
    val dimen_64: Dp = 0.dp,
    val dimen_72: Dp = 0.dp,
    val dimen_80: Dp = 0.dp,
    val dimen_100: Dp = 0.dp,
)

val LocalSizes = compositionLocalOf { Size() }

fun getSizes(): Size {
    return Size(
        dimen_0 = 0.dp,
        dimen_2 = 2.dp,
        dimen_4 = 4.dp,
        dimen_8 = 8.dp,
        dimen_16 = 16.dp,
        dimen_24 = 24.dp,
        dimen_32 = 32.dp,
        dimen_40 = 40.dp,
        dimen_48 = 48.dp,
        dimen_56 = 56.dp,
        dimen_64 = 64.dp,
        dimen_72 = 72.dp,
        dimen_80 = 80.dp,
        dimen_100 = 100.dp,
    )
}