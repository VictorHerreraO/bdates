package com.soyvictorherrera.bdates.modules.eventList.framework.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.soyvictorherrera.bdates.core.compose.modifier.conditional
import com.soyvictorherrera.bdates.core.compose.widget.CircularLoadingIndicator

private const val MIN_CONTENT_HEIGHT = 200

@Composable
fun LoadingSheetContent(
    modifier: Modifier = Modifier,
    fillMaxHeight: Boolean = false,
    minHeight: Dp = MIN_CONTENT_HEIGHT.dp,
) = Surface {
    Box(
        modifier = modifier
            .conditional(
                condition = fillMaxHeight,
                ifTrue = { fillMaxHeight() },
                ifFalse = { heightIn(min = minHeight) }
            )
            .fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        CircularLoadingIndicator()
    }
}
