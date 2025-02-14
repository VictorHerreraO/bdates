package com.soyvictorherrera.bdates.core.compose.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp)
)

val BottomSheetDialogShape: RoundedCornerShape
    @Composable get() = RoundedCornerShape(
        topStart = LocalSizes.current.dimen_16,
        topEnd = LocalSizes.current.dimen_16
    )

val BottomSheetContentShape: RoundedCornerShape
    @Composable get() = RoundedCornerShape(
        bottomStart = LocalSizes.current.dimen_16,
        bottomEnd = LocalSizes.current.dimen_16,
    )