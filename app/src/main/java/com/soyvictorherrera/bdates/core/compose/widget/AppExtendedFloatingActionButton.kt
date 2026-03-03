package com.soyvictorherrera.bdates.core.compose.widget

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.soyvictorherrera.bdates.core.compose.theme.Tradewind

@Composable
fun AppExtendedFloatingActionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        icon = { Icon(icon, contentDescription = contentDescription) },
        text = { Text(text = text) },
        containerColor = Tradewind,
        contentColor = MaterialTheme.colorScheme.onSecondary,
        shape = RoundedCornerShape(50),
        modifier = modifier
    )
}
