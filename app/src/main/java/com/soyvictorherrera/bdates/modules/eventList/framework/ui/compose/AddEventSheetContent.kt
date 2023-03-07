package com.soyvictorherrera.bdates.modules.eventList.framework.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.soyvictorherrera.bdates.R
import com.soyvictorherrera.bdates.core.compose.layout.BottomSheet
import com.soyvictorherrera.bdates.core.compose.layout.SpacerL
import com.soyvictorherrera.bdates.core.compose.layout.SpacerSm
import com.soyvictorherrera.bdates.core.compose.layout.SpacerXs
import com.soyvictorherrera.bdates.core.compose.modifier.clearFocusOnTap
import com.soyvictorherrera.bdates.core.compose.theme.BdatesTheme
import com.soyvictorherrera.bdates.modules.eventList.framework.presentation.AddEventViewState
import com.soyvictorherrera.bdates.modules.eventList.framework.presentation.EditMode
import java.time.LocalDate
import kotlinx.coroutines.flow.distinctUntilChanged

internal const val ICON_ALPHA = 0.6F
internal const val ICON_DISABLED_ALPHA = 0.2F

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun AddEventSheetContent(
    state: AddEventViewState,
    onEventNameChange: (String) -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    onYearSelected: (Int) -> Unit,
    onYearCleared: () -> Unit,
    onYearDisabled: (Boolean) -> Unit,
    onActionClick: () -> Unit,
    onBottomSheetDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) = BottomSheet(
    title = stringResource(
        when (state.editMode) {
            EditMode.CREATE -> R.string.add_event_title_create
            EditMode.EDIT -> R.string.add_event_title_edit
        }
    ),
    onBottomSheetDismiss = onBottomSheetDismiss,
    actions = {
        Button(
            onClick = onActionClick,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.secondary
            ),
            enabled = state.isSaveEnabled,
        ) {
            Text(
                text = stringResource(
                    when (state.editMode) {
                        EditMode.CREATE -> R.string.add_event_save_event
                        EditMode.EDIT -> R.string.add_event_save_changes
                    }
                )
            )
        }
    },
    modifier = modifier
        .nestedScroll(rememberNestedScrollInteropConnection())
        .verticalScroll(state = rememberScrollState())
        .clearFocusOnTap(),
) {
    Column {
        EventNameSection(
            eventName = state.eventName,
            onEventNameChange = onEventNameChange
        )

        SpacerL()

        EventDateSection(
            selectedDate = state.selectedDate,
            onDateSelected = onDateSelected
        )

        SpacerL()

        EventYearSection(
            selectedYear = state.selectedYear,
            isYearDisabled = state.isYearDisabled,
            onYearSelected = onYearSelected,
            onYearCleared = onYearCleared,
            onYearDisabled = onYearDisabled,
            validYearRange = state.validYearRange,
        )
    }
}

@Composable
private fun EventNameSection(
    eventName: String,
    onEventNameChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = null,
            tint = MaterialTheme.colors.secondary
        )

        SpacerXs()

        Text(text = stringResource(R.string.add_event_name), style = MaterialTheme.typography.body1)
    }

    SpacerSm()

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = eventName,
        onValueChange = onEventNameChange,
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            cursorColor = MaterialTheme.colors.secondary,
            focusedIndicatorColor = MaterialTheme.colors.secondary
        ),
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
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

        Text(text = stringResource(R.string.add_event_date), style = MaterialTheme.typography.body1)
    }

    SpacerSm()

    DateSelector(
        selectedDate = selectedDate,
        onDateSelected = onDateSelected,
    )
}

@Composable
fun EventYearSection(
    selectedYear: Int?,
    isYearDisabled: Boolean,
    onYearSelected: (Int) -> Unit,
    onYearCleared: () -> Unit,
    onYearDisabled: (Boolean) -> Unit,
    validYearRange: IntRange,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            Icons.Default.DateRange,
            contentDescription = null,
            tint = MaterialTheme.colors.secondary
        )

        SpacerXs()

        Text(text = stringResource(R.string.add_event_year), style = MaterialTheme.typography.body1)
    }

    SpacerSm()

    LaunchedEffect(isYearDisabled) {
        snapshotFlow { isYearDisabled }
            .distinctUntilChanged()
            .collect {
                onYearDisabled(it)
            }
    }

    YearSelector(
        selectedYear = selectedYear,
        onYearSelected = onYearSelected,
        onYearCleared = onYearCleared,
        enabled = isYearDisabled.not(),
        validYearRange = validYearRange,
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onYearDisabled(isYearDisabled.not())
            },
    ) {
        Checkbox(
            checked = isYearDisabled,
            onCheckedChange = onYearDisabled
        )

        SpacerSm()

        Text(
            stringResource(id = R.string.add_event_year_optional),
            style = MaterialTheme.typography.body2,
        )
    }
}

@Preview
@Composable
fun AddEventContentPreview() {
    BdatesTheme {
        AddEventSheetContent(
            state = AddEventViewState(
                eventName = "John Appleseed",
                selectedDate = LocalDate.now(),
                selectedYear = LocalDate.now().year,
                editMode = EditMode.CREATE,
                isYearDisabled = false,
                isLoading = false,
                validYearRange = 1900..2100,
            ),
            onEventNameChange = {},
            onDateSelected = {},
            onYearSelected = {},
            onYearCleared = {},
            onYearDisabled = {},
            onActionClick = {},
            onBottomSheetDismiss = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}