package com.soyvictorherrera.bdates.core.compose.widget

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp

@Composable
fun CircularLoadingIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.secondary,
    strokeWidth: Dp = ProgressIndicatorDefaults.StrokeWidth,
    backgroundColor: Color = Color.Transparent,
    strokeCap: StrokeCap = StrokeCap.Square,
) = CircularProgressIndicator(
    modifier = modifier,
    color = color,
    strokeWidth = strokeWidth,
    backgroundColor = backgroundColor,
    strokeCap = strokeCap
)
