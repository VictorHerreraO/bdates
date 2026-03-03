package com.soyvictorherrera.bdates.modules.eventList.framework.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.stringResource
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
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                AppExtendedFloatingActionButton(
                    text = stringResource(R.string.add_event),
                    icon = Icons.Filled.Add,
                    onClick = { onAction(EventListAction.AddEventClick) },
                    contentDescription = stringResource(R.string.add_event),
                    modifier = Modifier.padding(bottom = 16.dp),
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(topStart = LocalSizes.current.dimen_24, topEnd = LocalSizes.current.dimen_24),
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Permission warning banner
                    if (state.showMissingPermissionMessage) {
                        PermissionWarningBanner(
                            onBannerClick = { onAction(EventListAction.OpenAppSettings) },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    // Today's birthdays section
                    if (state.showTodayEvents) {
                        TodayEventListSection(
                            events = state.todayEvents,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    // Upcoming events section
                    UpcomingEventsSection(
                        state = state,
                        onAction = onAction,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
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
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.tertiary,
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
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.title_upcoming_events),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.padding(
                start = LocalSizes.current.dimen_16,
                end = LocalSizes.current.dimen_16,
                top = LocalSizes.current.dimen_16,
            ),
        )

        // Search field
        OutlinedTextField(
            value = state.query,
            onValueChange = { onAction(EventListAction.ChangeQuery(it)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            placeholder = { Text(stringResource(R.string.hint_search_event_by_name)) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LocalSizes.current.dimen_16, vertical = 8.dp),
        )

        if (state.showEmptyState) {
            // Empty state
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(LocalSizes.current.dimen_16),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(R.string.event_list_no_events_title),
                    style = MaterialTheme.typography.bodyLarge,
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
                    contentPadding = PaddingValues(bottom = 88.dp),
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
private fun PermissionWarningBanner(
    onBannerClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.errorContainer,
    ) {
        TextButton(
            onClick = onBannerClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
        ) {
            Text(
                text = stringResource(R.string.banner_notification_permission_not_granted_title),
                color = MaterialTheme.colorScheme.onErrorContainer,
                style = MaterialTheme.typography.bodySmall,
            )
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
                    EventViewState("1", "3", "days", "Dwight Schrute", "Friday, 03/06 • Turns 45"),
                    EventViewState("2", "12", "days", "Jim Halpert", "Sunday, 03/15 • Turns 38"),
                    EventViewState("3", "30", "days", "Pam Beesly", "Thursday, 04/02 • Turns 36"),
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
