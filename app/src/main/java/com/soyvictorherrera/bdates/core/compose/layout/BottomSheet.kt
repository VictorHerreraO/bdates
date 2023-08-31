package com.soyvictorherrera.bdates.core.compose.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.soyvictorherrera.bdates.core.compose.modifier.clearFocusOnTap
import com.soyvictorherrera.bdates.core.compose.theme.BdatesTheme
import com.soyvictorherrera.bdates.core.compose.theme.BottomSheetContentShape
import com.soyvictorherrera.bdates.core.compose.theme.BottomSheetDialogShape
import com.soyvictorherrera.bdates.core.compose.theme.LocalSizes

@Composable
fun BottomSheet(
    title: String,
    onBottomSheetDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    hasActions: Boolean = false,
    actions: (@Composable RowScope.() -> Unit)? = null,
    showLoadingIndicator: Boolean = false,
    loadingIndicator: (@Composable () -> Unit)? = null,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    content: @Composable () -> Unit,
) = Surface(
    shape = BottomSheetDialogShape,
    color = MaterialTheme.colors.background,
    elevation = LocalSizes.current.dimen_2
) {
    Box {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            val renderActions = hasActions && actions != null
            val scrollModifier = Modifier
                .nestedScroll(rememberNestedScrollInteropConnection())
                .verticalScroll(state = rememberScrollState())
                .clearFocusOnTap()
            val renderLoadingIndicator = showLoadingIndicator && loadingIndicator != null

            BottomSheetTopBar(
                title = title,
                onDismissClick = onBottomSheetDismiss,
            )

            if (renderLoadingIndicator) {
                loadingIndicator?.invoke()
                return@Surface
            }

            Column(
                modifier = modifier.then(scrollModifier)
            ) {
                Surface(
                    shape = if (renderActions) {
                        BottomSheetContentShape
                    } else {
                        RectangleShape
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(LocalSizes.current.dimen_32)
                    ) {
                        content()
                    }
                }

                if (renderActions && actions != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(LocalSizes.current.dimen_32),
                        content = actions
                    )
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun BottomSheetTopBar(
    title: String,
    onDismissClick: () -> Unit,
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp,
) = TopAppBar(
    title = { Text(text = title) },
    elevation = elevation,
    backgroundColor = MaterialTheme.colors.surface,
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
            hasActions = true,
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