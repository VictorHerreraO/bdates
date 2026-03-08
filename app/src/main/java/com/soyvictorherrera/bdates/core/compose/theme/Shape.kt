package com.soyvictorherrera.bdates.core.compose.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(28.dp)
)

val BottomSheetDialogShape: RoundedCornerShape =
    RoundedCornerShape(
        topStart = 28.dp,
        topEnd = 28.dp
    )

val BottomSheetContentShape: RoundedCornerShape =
    RoundedCornerShape(
        bottomStart = 16.dp,
        bottomEnd = 16.dp,
    )
