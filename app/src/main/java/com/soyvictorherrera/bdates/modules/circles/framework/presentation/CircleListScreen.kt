package com.soyvictorherrera.bdates.modules.circles.framework.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.soyvictorherrera.bdates.R
import com.soyvictorherrera.bdates.core.compose.theme.BdatesTheme
import com.soyvictorherrera.bdates.core.compose.theme.Bossanova
import com.soyvictorherrera.bdates.core.compose.theme.LocalSizes
import com.soyvictorherrera.bdates.core.compose.theme.Tradewind
import com.soyvictorherrera.bdates.core.compose.theme.White
import com.soyvictorherrera.bdates.modules.circles.domain.model.Circle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CircleListScreen(
    circles: List<Circle>,
    activeCircleId: String?,
    onAddCircleClick: () -> Unit,
    onCircleClick: (Circle) -> Unit,
    onMenuClick: (Circle) -> Unit,
    modifier: Modifier = Modifier
) {
    val layoutDirection = androidx.compose.ui.platform.LocalLayoutDirection.current

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primary
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
            floatingActionButtonPosition = FabPosition.Center,
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = onAddCircleClick,
                    icon = { Icon(Icons.Filled.Add, contentDescription = "Add Circle") },
                    text = { Text(text = "ADD CIRCLE") },
                    containerColor = Tradewind,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(50),
                    modifier = Modifier.padding(bottom = 16.dp) 
                )
            }
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = innerPadding.calculateTopPadding(),
                        bottom = innerPadding.calculateBottomPadding(),
                        start = innerPadding.calculateStartPadding(layoutDirection),
                        end = innerPadding.calculateEndPadding(layoutDirection)
                    ),
                color = MaterialTheme.colorScheme.surface,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(
                    topStart = LocalSizes.current.dimen_24, 
                    topEnd = LocalSizes.current.dimen_24
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = LocalSizes.current.dimen_16)
                ) {
                    Text(
                        text = "My Circles",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = LocalSizes.current.dimen_16)
                    )
                    Spacer(modifier = Modifier.height(LocalSizes.current.dimen_16))

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            bottom = LocalSizes.current.dimen_16 + 80.dp 
                        )
                    ) {
                        items(circles) { circle ->
                            CircleListItem(
                                circle = circle,
                                isActive = circle.id == activeCircleId,
                                onCircleClick = onCircleClick,
                                onMenuClick = onMenuClick
                            )
                        }
                    }
                }
            }
        }
    }
}

// Mock Data Provider
val mockCircles = listOf(
    Circle(id = "1", name = "Family", description = "My awesome family", isLocalOnly = true, isDefaultCircle = true, updateDate = null),
    Circle(id = "2", name = "Close Friends", description = "College besties", isLocalOnly = true, isDefaultCircle = false, updateDate = null),
    Circle(id = "3", name = "Work", description = "Colleagues from the office", isLocalOnly = true, isDefaultCircle = false, updateDate = null),
    Circle(id = "4", name = "Gym Buddies", description = "Workout group", isLocalOnly = true, isDefaultCircle = false, updateDate = null)
)

@Preview(showBackground = true)
@Composable
fun PreviewCircleListScreen() {
    BdatesTheme {
        CircleListScreen(
            circles = mockCircles,
            activeCircleId = "1",
            onAddCircleClick = {},
            onCircleClick = {},
            onMenuClick = {}
        )
    }
}
