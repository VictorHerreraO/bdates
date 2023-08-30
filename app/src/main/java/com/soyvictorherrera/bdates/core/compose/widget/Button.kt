package com.soyvictorherrera.bdates.core.compose.widget

import androidx.annotation.StringRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.soyvictorherrera.bdates.core.compose.theme.LocalSizes
import com.soyvictorherrera.bdates.core.compose.theme.Salmon

@Composable
fun PrimaryActionButton(
    @StringRes text: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    showLoader: Boolean = false,
) = Button(
    onClick = onClick,
    modifier = modifier.heightIn(min = 48.dp),
    colors = primaryActionButtonColors(),
    enabled = enabled,
) {
    if (showLoader) {
        CircularLoadingIndicator(
            modifier = Modifier.size(LocalSizes.current.dimen_32)
        )
    } else {
        Text(
            text = stringResource(text),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun primaryActionButtonColors(): ButtonColors = ButtonDefaults.buttonColors(
    backgroundColor = MaterialTheme.colors.secondary,
)

@Composable
fun DeleteActionButton(
    @StringRes text: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) = TextButton(
    onClick = onClick,
    modifier = modifier.heightIn(min = 48.dp),
    colors = deleteActionButtonColors(),
    enabled = enabled,
) {
    Text(
        text = stringResource(text),
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun deleteActionButtonColors(): ButtonColors = ButtonDefaults.textButtonColors(
    contentColor = if (isSystemInDarkTheme()) {
        MaterialTheme.colors.error
    } else {
        Salmon
    }
)
