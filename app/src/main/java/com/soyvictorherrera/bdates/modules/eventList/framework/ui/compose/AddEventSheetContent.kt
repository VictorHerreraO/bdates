package com.soyvictorherrera.bdates.modules.eventList.framework.ui.compose

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.soyvictorherrera.bdates.core.compose.layout.BottomSheet
import com.soyvictorherrera.bdates.core.compose.layout.SpacerL
import com.soyvictorherrera.bdates.core.compose.layout.SpacerM
import com.soyvictorherrera.bdates.core.compose.layout.SpacerSm
import com.soyvictorherrera.bdates.core.compose.layout.SpacerXs
import com.soyvictorherrera.bdates.core.compose.theme.BdatesTheme
import java.time.LocalDate

internal const val ICON_ALPHA = 0.6F
internal const val ICON_DISABLED_ALPHA = 0.38F

@Composable
fun AddEventSheetContent(
    modifier: Modifier = Modifier,
) = BottomSheet(
    modifier = modifier
) {
    Column(
        modifier = Modifier.scrollable(
            state = rememberScrollState(),
            orientation = Orientation.Vertical
        )
    ) {
        var selectedDate: LocalDate by remember { mutableStateOf(LocalDate.now()) }
        val onDateSelected: (LocalDate) -> Unit = remember { { selectedDate = it } }

        EventNameSection()

        SpacerM()

        EventDateSection(
            selectedDate = selectedDate,
            onDateSelected = onDateSelected
        )

        SpacerM()

        EventYearSection(
            selectedDate = selectedDate,
            onDateSelected = onDateSelected
        )

        SpacerL()

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
        ) {
            Text(text = "Create event")
        }
    }
}

@Composable
private fun EventNameSection() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = null,
            tint = MaterialTheme.colors.secondary
        )

        SpacerXs()

        Text(text = "Event name", style = MaterialTheme.typography.body1)
    }

    SpacerSm()

    val hi = remember { mutableStateOf("Hi!") }
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = hi.value,
        onValueChange = { hi.value = it },
    )
}

@Composable
fun EventDateSection(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = null,
            tint = MaterialTheme.colors.secondary
        )

        SpacerXs()

        Text(text = "Event date", style = MaterialTheme.typography.body1)
    }

    SpacerSm()

    DateSelector(
        selectedDate = selectedDate,
        onDateSelected = onDateSelected,
    )
}

@Composable
fun EventYearSection(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            Icons.Default.DateRange,
            contentDescription = null,
            tint = MaterialTheme.colors.secondary
        )

        SpacerXs()

        Text(text = "Event Year", style = MaterialTheme.typography.body1)
    }

    SpacerSm()

    var yearDisabled by remember { mutableStateOf(false) }
    YearSelector(
        selectedDate = selectedDate,
        onDateSelected = onDateSelected,
        enabled = yearDisabled.not(),
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Checkbox(
            checked = yearDisabled,
            onCheckedChange = { yearDisabled = it }
        )

        SpacerSm()

        Text(
            text = "My event has no year",
            style = MaterialTheme.typography.body2
        )
    }
}

@Preview
@Composable
fun AddEventContentPreview() {
    BdatesTheme {
        AddEventSheetContent(
            modifier = Modifier.fillMaxWidth()
        )
    }
}