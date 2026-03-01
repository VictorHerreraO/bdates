package com.soyvictorherrera.bdates.modules.eventList.framework.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.soyvictorherrera.bdates.R
import com.soyvictorherrera.bdates.core.compose.modifier.conditional
import com.soyvictorherrera.bdates.core.compose.theme.LocalSizes
import com.soyvictorherrera.bdates.core.compose.widget.CircularLoadingIndicator

private const val MIN_CONTENT_HEIGHT = 200

//region Loading Content
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
//endregion

//region Error Content
@Composable
fun ErrorSheetContent(
    modifier: Modifier = Modifier,
    minHeight: Dp = MIN_CONTENT_HEIGHT.dp,
) = Surface {
    Box(
        modifier = modifier
            .heightIn(min = minHeight)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.emoji_face_with_spiral_eyes),
            contentDescription = null,
            modifier = Modifier.size(LocalSizes.current.dimen_100)
        )
    }
}
//endregion
