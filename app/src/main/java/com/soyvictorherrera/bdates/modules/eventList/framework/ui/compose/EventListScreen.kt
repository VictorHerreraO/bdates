package com.soyvictorherrera.bdates.modules.eventList.framework.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.graphics.Color
import com.soyvictorherrera.bdates.core.compose.theme.Gallery
import com.soyvictorherrera.bdates.core.compose.theme.Paradiso
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.soyvictorherrera.bdates.R
import com.soyvictorherrera.bdates.core.compose.theme.BdatesTheme
import com.soyvictorherrera.bdates.core.compose.theme.LocalSizes
import com.soyvictorherrera.bdates.core.compose.widget.AppExtendedFloatingActionButton
import com.soyvictorherrera.bdates.modules.eventList.framework.presentation.EventListAction
import com.soyvictorherrera.bdates.modules.eventList.framework.presentation.EventListState
import com.soyvictorherrera.bdates.modules.eventList.framework.presentation.EventViewState
import com.soyvictorherrera.bdates.modules.eventList.framework.presentation.TodayEventViewState
import com.soyvictorherrera.bdates.core.compose.theme.Rajah
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Alignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListScreen(
    state: EventListState,
    onAction: (EventListAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Show snackbar when error message is set
    state.errorMessage?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message)
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primary,
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
            floatingActionButtonPosition = FabPosition.Center,
            floatingActionButton = {
                AppExtendedFloatingActionButton(
                    text = stringResource(R.string.add_event).uppercase(),
                    icon = Icons.Filled.Add,
                    onClick = { onAction(EventListAction.AddEventClick) },
                    contentDescription = stringResource(R.string.add_event),
                    modifier = Modifier.padding(bottom = 16.dp),
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding())
            ) {
                // Today's birthdays section
                if (state.showTodayEvents) {
                    TodayEventListSection(
                        events = state.todayEvents,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(
                        topStart = 32.dp,
                        topEnd = 32.dp
                    ),
                ) {
                    // Upcoming events section
                    UpcomingEventsSection(
                        state = state,
                        onAction = onAction,
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(
                            bottom = 88.dp + innerPadding.calculateBottomPadding()
                        ),
                    )
                }
            }
        }
    }
}

@Composable
private fun TodayEventListSection(
    events: List<TodayEventViewState>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(top = LocalSizes.current.dimen_16)) {
        Text(
            text = stringResource(R.string.title_today_occasions),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(horizontal = LocalSizes.current.dimen_16),
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = LocalSizes.current.dimen_16, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(events, key = { it.id }) { event ->
                TodayEventItem(event = event)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UpcomingEventsSection(
    state: EventListState,
    onAction: (EventListAction) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        modifier = modifier.padding(top = 16.dp)
    ) {
        if (state.showMissingPermissionMessage) {
            PermissionWarningBanner(
                onBannerClick = { onAction(EventListAction.OpenAppSettings) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }

        if (!state.showEmptyState) {
            Text(
                text = stringResource(R.string.title_upcoming_events),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(
                    start = LocalSizes.current.dimen_16,
                    end = LocalSizes.current.dimen_16,
                    top = 24.dp, // Slightly more padding for the larger radius
                    bottom = 4.dp
                ),
            )

            // Search field
            SearchBar(
                query = state.query,
                onQueryChange = { onAction(EventListAction.ChangeQuery(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = LocalSizes.current.dimen_16, vertical = 4.dp)
            )
        }

        if (state.showEmptyState) {
            // Empty state
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
                    .padding(horizontal = LocalSizes.current.dimen_16),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.size(120.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.vector_balloon),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        alpha = 0.7f
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = stringResource(R.string.event_list_no_events_title),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.event_list_no_events_description),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                )
            }
        } else {
            PullToRefreshBox(
                isRefreshing = state.isRefreshing,
                onRefresh = { onAction(EventListAction.Refresh) },
                modifier = Modifier.weight(1f),
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = contentPadding,
                ) {
                    items(state.events, key = { it.id }) { event ->
                        UpcomingEventItem(
                            event = event,
                            onClick = { id -> onAction(EventListAction.EventClick(id)) },
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        placeholder = {
            Text(
                text = stringResource(R.string.hint_search_event_by_name),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
            cursorColor = Paradiso
        ),
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
private fun PermissionWarningBanner(
    onBannerClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onBannerClick,
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp)),
        color = Rajah,
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "⛔️",
                modifier = Modifier.padding(end = 12.dp)
            )
            Column {
                Text(
                    text = stringResource(R.string.banner_notification_permission_not_granted_title)
                        .replace("⛔️ ", ""),
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.banner_notification_permission_not_granted_subtitle),
                    color = Color.Black,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

// ---- Previews ----

@Preview(showBackground = true)
@Composable
private fun PreviewEventListScreenWithData() {
    BdatesTheme {
        EventListScreen(
            state = EventListState(
                events = listOf(
                    EventViewState("1", "3", "days", "Dwight Schrute", "Friday, 03/06 • Turns 45", "🎂"),
                    EventViewState("2", "12", "days", "Jim Halpert", "Sunday, 03/15 • Turns 38", "🎂"),
                    EventViewState("3", "30", "days", "Pam Beesly", "Thursday, 04/02 • Turns 36", "🎂"),
                ),
                todayEvents = listOf(
                    TodayEventViewState("4", "41", "Michael Scott", "Birthday"),
                ),
            ),
            onAction = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewEventListScreenEmpty() {
    BdatesTheme {
        EventListScreen(
            state = EventListState(),
            onAction = {},
        )
    }
}
