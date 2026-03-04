package com.soyvictorherrera.bdates.modules.eventList.framework.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soyvictorherrera.bdates.core.compose.theme.BdatesTheme
import com.soyvictorherrera.bdates.core.compose.theme.Dolphin
import com.soyvictorherrera.bdates.core.compose.theme.Tradewind
import com.soyvictorherrera.bdates.core.compose.theme.White
import com.soyvictorherrera.bdates.modules.eventList.framework.presentation.EventViewState
import com.soyvictorherrera.bdates.modules.eventList.framework.presentation.TodayEventViewState

@Composable
fun UpcomingEventItem(
    event: EventViewState,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 72.dp)
            .clickable { onClick(event.id) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Joined Countdown and Emoji badges
        Surface(
            modifier = Modifier.wrapContentSize(),
            color = Color.Transparent,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Countdown part
                Surface(
                    modifier = Modifier.size(56.dp),
                    color = Tradewind,
                    shape = RoundedCornerShape(
                        topStart = 8.dp,
                        bottomStart = 8.dp,
                        topEnd = 0.dp,
                        bottomEnd = 0.dp
                    ),
                ) {
                    Column(
                        modifier = Modifier.padding(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = event.remainingTimeValue,
                            style = MaterialTheme.typography.titleLarge,
                            color = White,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = event.remainingTimeUnit.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = White,
                        )
                    }
                }

                // Emoji part
                Surface(
                    modifier = Modifier.size(56.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        bottomStart = 0.dp,
                        topEnd = 8.dp,
                        bottomEnd = 8.dp
                    ),
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = event.eventEmoji,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Name + description
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = event.name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = event.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun TodayEventItem(
    event: TodayEventViewState,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.size(120.dp),
        color = Dolphin,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 4.dp,
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            event.friendAge?.let { age ->
                Text(
                    text = age,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = com.soyvictorherrera.bdates.core.compose.theme.Rajah,
                )
            }
            Text(
                text = event.friendName,
                style = MaterialTheme.typography.bodyMedium,
                color = White,
                fontWeight = FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = event.eventType.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = White.copy(alpha = 0.8f),
                fontWeight = FontWeight.Normal,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewUpcomingEventItem() {
    BdatesTheme {
        UpcomingEventItem(
            event = EventViewState(
                id = "1",
                remainingTimeValue = "6",
                remainingTimeUnit = "days",
                name = "Dwight Schrute",
                description = "Monday, 03/21 • Turns 37",
                eventEmoji = "🎂",
            ),
            onClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewTodayEventItem() {
    BdatesTheme {
        TodayEventItem(
            event = TodayEventViewState(
                id = "1",
                friendAge = "41",
                friendName = "Michael Scott",
                eventType = "Birthday",
            ),
        )
    }
}
