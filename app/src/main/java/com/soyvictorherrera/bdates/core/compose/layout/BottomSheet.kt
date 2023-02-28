package com.soyvictorherrera.bdates.core.compose.layout

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.soyvictorherrera.bdates.core.compose.theme.BdatesTheme
import com.soyvictorherrera.bdates.core.compose.theme.BottomSheetDialogShape
import com.soyvictorherrera.bdates.core.compose.theme.LocalSizes

@Composable
fun BottomSheet(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) = Surface(
    modifier = modifier,
    shape = BottomSheetDialogShape,
) {
    Box(
        modifier = Modifier
            .padding(
                top = LocalSizes.current.dimen_32,
                bottom = LocalSizes.current.dimen_16,
                start = LocalSizes.current.dimen_32,
                end = LocalSizes.current.dimen_32,
            )
    ) {
        content()
    }
}

@Composable
@Preview
private fun BottomSheetPreview() {
    BdatesTheme {
        BottomSheet {}
    }
}