package com.soyvictorherrera.bdates.core.compose.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.soyvictorherrera.bdates.core.compose.theme.BdatesTheme
import com.soyvictorherrera.bdates.core.compose.theme.BottomSheetContentShape
import com.soyvictorherrera.bdates.core.compose.theme.BottomSheetDialogShape
import com.soyvictorherrera.bdates.core.compose.theme.LocalSizes

@Composable
fun BottomSheet(
    title: String,
    onBottomSheetDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    actions: (@Composable RowScope.() -> Unit)? = null,
    content: @Composable () -> Unit,
) = Surface(
    modifier = modifier,
    shape = BottomSheetDialogShape,
    color = MaterialTheme.colors.background,
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            elevation = LocalSizes.current.dimen_4,
            shape = if (actions != null) {
                BottomSheetContentShape
            } else {
                BottomSheetDialogShape
            },
            color = MaterialTheme.colors.surface,
        ) {
            Column {
                BottomSheetTopBar(
                    title = title,
                    onDismissClick = onBottomSheetDismiss
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(LocalSizes.current.dimen_32)
                ) {
                    content()
                }
            }
        }
        actions?.let {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(LocalSizes.current.dimen_32),
                content = it
            )
        }
    }
}

@Composable
private fun BottomSheetTopBar(
    title: String,
    onDismissClick: () -> Unit,
    modifier: Modifier = Modifier,
) = TopAppBar(
    title = { Text(text = title) },
    elevation = 0.dp,
    backgroundColor = Color.Unspecified,
    navigationIcon = {
        IconButton(onClick = onDismissClick) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close"
            )
        }
    },
    modifier = modifier,
)

@Composable
@Preview
private fun BottomSheetWithActionsPreview() {
    BdatesTheme {
        BottomSheet(
            title = "Bottom sheet",
            onBottomSheetDismiss = {},
            actions = {
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.secondary
                    )
                ) {
                    Text(text = "Action")
                }
            }
        ) {
            Text(text = "Content")
        }
    }
}

@Composable
@Preview
private fun BottomSheetWithNoActionsPreview() {
    BdatesTheme {
        BottomSheet(
            title = "Bottom sheet",
            onBottomSheetDismiss = {},
        ) {
            Text(text = "Content")
        }
    }
}