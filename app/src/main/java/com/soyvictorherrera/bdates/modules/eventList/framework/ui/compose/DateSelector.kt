package com.soyvictorherrera.bdates.modules.eventList.framework.ui.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.soyvictorherrera.bdates.core.compose.layout.SpacerM
import com.soyvictorherrera.bdates.core.compose.layout.SpacerSm
import com.soyvictorherrera.bdates.core.compose.modifier.conditional
import com.soyvictorherrera.bdates.core.compose.theme.BdatesTheme
import com.soyvictorherrera.bdates.core.compose.theme.LocalSizes
import com.soyvictorherrera.bdates.core.compose.theme.Shapes
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

private const val DAYS_IN_WEEK = 7
private const val MONTHS_IN_YEAR = 12

@Composable
fun DateSelector(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    border: BorderStroke? = null,
    elevation: Dp = 0.dp,
) = Surface(
    modifier = modifier,
    shape = Shapes.medium,
    border = border,
    elevation = elevation,
) {
    Column(
        modifier = Modifier.fillMaxWidth(1f)
    ) {
        var backingDate: LocalDate by remember { mutableStateOf(selectedDate) }
        val visibleDate: LocalDate by remember(selectedDate) {
            derivedStateOf { backingDate.withYear(selectedDate.year) }
        }

        MonthSelector(
            selectedMonth = visibleDate.month,
            onSelectedMonthChange = { backingDate = visibleDate.withMonth(it.value) }
        )

        SpacerM()

        DayNamesHeader()

        SpacerSm()

        CalendarGrid(
            visibleDate = visibleDate,
            onVisibleDateChange = { backingDate = it },
            selectedDate = selectedDate,
            onSelectedDateChange = onDateSelected,
        )
    }
}

@Composable
private fun MonthSelector(
    selectedMonth: Month,
    onSelectedMonthChange: (Month) -> Unit,
    modifier: Modifier = Modifier,
) = Surface(
    modifier = modifier.fillMaxWidth(),
    shape = LocalSizes.current.run {
        RoundedCornerShape(
            topStart = dimen_4,
            topEnd = dimen_4,
        )
    },
    color = MaterialTheme.colors.onSurface.copy(alpha = TextFieldDefaults.BackgroundOpacity),
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = LocalSizes.current.dimen_4,
            ),
    ) {
        IconButton(
            enabled = (selectedMonth > Month.JANUARY),
            onClick = { onSelectedMonthChange(selectedMonth.minus(1)) },
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Prev",
                tint = MaterialTheme.colors.onSurface.copy(alpha = ICON_ALPHA),
            )
        }

        Text(
            text = selectedMonth.getDisplayName(TextStyle.FULL, Locale.getDefault()),
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
        )

        IconButton(
            enabled = (selectedMonth < Month.DECEMBER),
            onClick = { onSelectedMonthChange(selectedMonth.plus(1)) },
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Next",
                tint = MaterialTheme.colors.onSurface.copy(alpha = ICON_ALPHA),
            )
        }
    }
}

@Composable
private fun DayNamesHeader(
    modifier: Modifier = Modifier,
) = Row(modifier = modifier) {
    val weekDays = remember {
        listOf(
            DayOfWeek.SUNDAY,
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY,
        )
    }
    weekDays.forEach {
        DayName(
            text = it.getDisplayName(TextStyle.NARROW_STANDALONE, Locale.getDefault()),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun DayName(
    text: String,
    modifier: Modifier = Modifier,
) = Text(
    text = text,
    modifier = modifier,
    textAlign = TextAlign.Center,
    fontWeight = FontWeight.Light
)

@Composable
private fun CalendarGrid(
    visibleDate: LocalDate,
    onVisibleDateChange: (LocalDate) -> Unit,
    selectedDate: LocalDate,
    onSelectedDateChange: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(selectedDate) {
        coroutineScope.launch {
            listState.scrollToItem(selectedDate.monthValue.dec())
        }
    }

    LazyRow(
        modifier = modifier,
        state = listState,
        userScrollEnabled = false,
    ) {
        items(count = MONTHS_IN_YEAR, key = { it }) {
            CalendarItem(
                visibleDate = visibleDate,
                selectedDate = selectedDate,
                onSelectedDateChange = onSelectedDateChange,
                modifier = Modifier
                    .fillParentMaxWidth(1f)
                    .animateContentSize()
            )
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { onVisibleDateChange(visibleDate.withMonth(it.inc())) }
    }
}

@Composable
private fun CalendarItem(
    visibleDate: LocalDate,
    selectedDate: LocalDate,
    onSelectedDateChange: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val offset = remember(visibleDate) {
        when (visibleDate.withDayOfMonth(1).dayOfWeek!!) {
            DayOfWeek.SUNDAY -> 0
            DayOfWeek.MONDAY -> 1
            DayOfWeek.TUESDAY -> 2
            DayOfWeek.WEDNESDAY -> 3
            DayOfWeek.THURSDAY -> 4
            DayOfWeek.FRIDAY -> 5
            DayOfWeek.SATURDAY -> 6
        }
    }
    val monthRange = remember(visibleDate) {
        offset until visibleDate.lengthOfMonth().plus(offset)
    }
    val lengthOfMonth = remember(visibleDate) { visibleDate.lengthOfMonth().plus(offset) }

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(DAYS_IN_WEEK),
        userScrollEnabled = false,
    ) {
        items(lengthOfMonth) { index ->
            if (index in monthRange) {
                val day = index.minus(offset).plus(1)
                DayValue(
                    value = day,
                    selected = (selectedDate.month == visibleDate.month && day == selectedDate.dayOfMonth),
                    onSelectionChange = { onSelectedDateChange(visibleDate.withDayOfMonth(it)) }
                )
            }
        }
    }
}

@Composable
private fun DayValue(
    value: Int,
    selected: Boolean,
    onSelectionChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) = IconToggleButton(
    checked = selected,
    onCheckedChange = { onSelectionChange(value) },
    modifier = modifier
        .conditional(selected) {
            background(
                color = MaterialTheme.colors.secondary,
                shape = Shapes.small
            )
        }
) {
    if (selected) {
        Text(
            text = "$value",
            color = MaterialTheme.colors.onSecondary,
            fontWeight = FontWeight.Bold
        )
    } else {
        Text(text = "$value")
    }
}


@Composable
@Preview(
    showBackground = true,
)
private fun DateSelectorPreviewLight() {
    BdatesTheme(darkTheme = false) {
        var selectedDate by remember { mutableStateOf(LocalDate.now()) }
        DateSelector(
            selectedDate = selectedDate,
            onDateSelected = { selectedDate = it }
        )
    }
}

@Composable
@Preview(
    showBackground = true,
)
private fun DateSelectorPreviewDark() {
    BdatesTheme(darkTheme = true) {
        var selectedDate by remember { mutableStateOf(LocalDate.now()) }
        DateSelector(
            selectedDate = selectedDate,
            onDateSelected = { selectedDate = it }
        )
    }
}