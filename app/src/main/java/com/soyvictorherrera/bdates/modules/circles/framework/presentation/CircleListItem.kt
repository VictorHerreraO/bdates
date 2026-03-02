package com.soyvictorherrera.bdates.modules.circles.framework.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.soyvictorherrera.bdates.R
import com.soyvictorherrera.bdates.core.compose.theme.*
import com.soyvictorherrera.bdates.modules.circles.domain.model.Circle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CircleListItem(
    circle: Circle,
    isActive: Boolean,
    onCircleClick: (Circle) -> Unit,
    onMenuClick: (Circle) -> Unit,
    modifier: Modifier = Modifier
) {
    // No special background or content color for active item as per user request
    val backgroundColor = MaterialTheme.colorScheme.surface
    val contentColor = MaterialTheme.colorScheme.onSurface

    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = LocalSizes.current.dimen_72)
            .background(backgroundColor)
            .clickable { onCircleClick(circle) }
            .padding(horizontal = LocalSizes.current.dimen_16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = circle.name,
                style = MaterialTheme.typography.bodyLarge,
                color = contentColor,
                fontWeight = FontWeight.Normal
            )
            if (!circle.description.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(LocalSizes.current.dimen_4))
                Text(
                    text = circle.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = contentColor.copy(alpha = 0.6f)
                )
            }
        }
            
        if (isActive) {
            Box(
                modifier = Modifier.size(LocalSizes.current.dimen_48),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Active Circle",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(LocalSizes.current.dimen_24)
                )
            }
        } else {
            IconButton(onClick = { onMenuClick(circle) }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Circle Options",
                    tint = contentColor
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCircleListItemActive() {
    BdatesTheme {
        CircleListItem(
            circle = Circle(
                id = "1",
                name = "Family",
                description = "My awesome family",
                isLocalOnly = true,
                isDefaultCircle = true,
                updateDate = null
            ),
            isActive = true,
            onCircleClick = {},
            onMenuClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCircleListItemInactive() {
    BdatesTheme {
        CircleListItem(
            circle = Circle(
                id = "2",
                name = "Friends",
                description = "Close friends",
                isLocalOnly = true,
                isDefaultCircle = false,
                updateDate = null
            ),
            isActive = false,
            onCircleClick = {},
            onMenuClick = {}
        )
    }
}
