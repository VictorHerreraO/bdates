package com.soyvictorherrera.bdates.modules.eventList.framework.ui.compose

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import com.soyvictorherrera.bdates.R
import com.soyvictorherrera.bdates.core.compose.layout.BottomSheet
import com.soyvictorherrera.bdates.core.compose.layout.SpacerL
import com.soyvictorherrera.bdates.core.compose.layout.SpacerSm
import com.soyvictorherrera.bdates.core.compose.layout.SpacerXs
import com.soyvictorherrera.bdates.core.compose.theme.BdatesTheme
import com.soyvictorherrera.bdates.core.compose.widget.DeleteActionButton
import com.soyvictorherrera.bdates.core.compose.widget.PrimaryActionButton
import com.soyvictorherrera.bdates.core.event.asConsumableEvent
import com.soyvictorherrera.bdates.core.event.consumeValue
import com.soyvictorherrera.bdates.modules.eventList.framework.presentation.AddEventError
import com.soyvictorherrera.bdates.modules.eventList.framework.presentation.AddEventViewState
import com.soyvictorherrera.bdates.modules.eventList.framework.presentation.EditMode
import kotlinx.coroutines.flow.distinctUntilChanged
import java.time.LocalDate

internal const val ICON_ALPHA = 0.6F
internal const val ICON_DISABLED_ALPHA = 0.2F

//region Content
@Composable
fun AddEventSheetContent(
    state: AddEventViewState,
    onEventNameChange: (String) -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    onYearSelected: (Int) -> Unit,
    onYearCleared: () -> Unit,
    onYearDisabled: (Boolean) -> Unit,
    onActionClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onBottomSheetDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) = BottomSheet(
    title = stringResource(
        when (state.editMode) {
            EditMode.CREATE -> R.string.add_event_title_create
            EditMode.EDIT -> R.string.add_event_title_edit
        }
    ),
    onBottomSheetDismiss = onBottomSheetDismiss,
    hasActions = true,
    actions = {
        EventActions(
            state = state,
            onActionClick = onActionClick,
            onDeleteClick = onDeleteClick
        )
    },
    modifier = modifier,
    snackbarHostState = snackbarHostState,
) {
    when {
        state.isLoading -> LoadingAddEventSheetContent()

        state.isError -> ErrorAddEventSheetContent()

        state.isSuccess -> SuccessAddEventSheetContent(
            state = state,
            onEventNameChange = onEventNameChange,
            onDateSelected = onDateSelected,
            onYearSelected = onYearSelected,
            onYearCleared = onYearCleared,
            onYearDisabled = onYearDisabled,
        )
    }

    val context = LocalContext.current
    LaunchedEffect(state.error) {
        state.error.consumeValue { error ->
            error.showMessage(
                context = context,
                snackbarHostState = snackbarHostState
            )
        }
    }
}

@Composable
private fun LoadingAddEventSheetContent(
    modifier: Modifier = Modifier,
) = LoadingSheetContent(modifier = modifier)

@Composable
private fun ErrorAddEventSheetContent(
    modifier: Modifier = Modifier,
) = ErrorSheetContent(modifier = modifier)

@Composable
private fun SuccessAddEventSheetContent(
    state: AddEventViewState,
    onEventNameChange: (String) -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    onYearSelected: (Int) -> Unit,
    onYearCleared: () -> Unit,
    onYearDisabled: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) = Column(modifier = modifier) {
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
//endregion

//region Components
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

        Text(
            text = stringResource(R.string.add_event_name),
            style = MaterialTheme.typography.body1
        )
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

        Text(
            text = stringResource(R.string.add_event_date),
            style = MaterialTheme.typography.body1
        )
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

        Text(
            text = stringResource(R.string.add_event_year),
            style = MaterialTheme.typography.body1
        )
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

@Composable
private fun EventActions(
    state: AddEventViewState,
    onActionClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state.editMode) {
        EditMode.CREATE -> {
            Row(modifier = modifier.fillMaxWidth()) {
                PrimaryActionButton(
                    text = R.string.add_event_save_event,
                    onClick = onActionClick,
                    modifier = Modifier.weight(1f),
                    enabled = state.isSaveEnabled,
                    showLoader = state.isSaving
                )
            }
        }

        EditMode.EDIT -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.fillMaxWidth()
            ) {
                PrimaryActionButton(
                    text = R.string.add_event_save_changes,
                    onClick = onActionClick,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.isSaveEnabled,
                    showLoader = state.isSaving
                )

                SpacerL()

                DeleteActionButton(
                    text = R.string.add_event_delete_event,
                    onClick = onDeleteClick,
                    enabled = state.isDeleteEnabled,
                    showLoader = state.isDeleting
                )
            }
        }
    }
}
//endregion

//region Error messages
private suspend fun AddEventError.showMessage(
    context: Context,
    snackbarHostState: SnackbarHostState,
): Unit = with(context) {
    when (this@showMessage) {
        AddEventError.ERROR_LOADING -> snackbarHostState.showSnackbar(
            message = getString(R.string.add_event_error_loading),
            duration = SnackbarDuration.Indefinite,
        )

        AddEventError.ERROR_SAVING_EVENT -> snackbarHostState.showSnackbar(
            message = getString(R.string.add_event_error_saving),
            duration = SnackbarDuration.Short,
        )

        AddEventError.ERROR_DELETING_EVENT -> snackbarHostState.showSnackbar(
            message = getString(R.string.add_event_error_deleting),
            duration = SnackbarDuration.Short,
        )
    }
}
//endregion

//region Previews
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
                isSaving = true,
                validYearRange = 1900..2100,
            ),
            onEventNameChange = {},
            onDateSelected = {},
            onYearSelected = {},
            onYearCleared = {},
            onYearDisabled = {},
            onActionClick = {},
            onDeleteClick = {},
            onBottomSheetDismiss = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
fun EditEventContentPreview() {
    BdatesTheme(darkTheme = true) {
        AddEventSheetContent(
            state = AddEventViewState(
                eventName = "John Appleseed",
                selectedDate = LocalDate.now(),
                selectedYear = LocalDate.now().year,
                editMode = EditMode.EDIT,
                isYearDisabled = false,
                isLoading = false,
                isSaving = false,
                validYearRange = 1900..2100,
            ),
            onEventNameChange = {},
            onDateSelected = {},
            onYearSelected = {},
            onYearCleared = {},
            onYearDisabled = {},
            onActionClick = {},
            onDeleteClick = {},
            onBottomSheetDismiss = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
fun LoadingEventContentPreview() {
    BdatesTheme {
        AddEventSheetContent(
            state = AddEventViewState(
                eventName = "",
                selectedDate = LocalDate.now(),
                selectedYear = LocalDate.now().year,
                editMode = EditMode.EDIT,
                isYearDisabled = false,
                isLoading = true,
                isSaving = false,
                validYearRange = 1900..2100,
            ),
            onEventNameChange = {},
            onDateSelected = {},
            onYearSelected = {},
            onYearCleared = {},
            onYearDisabled = {},
            onActionClick = {},
            onDeleteClick = {},
            onBottomSheetDismiss = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
fun ErrorEventContentPreview() {
    BdatesTheme {
        AddEventSheetContent(
            state = AddEventViewState(
                eventName = "",
                selectedDate = LocalDate.now(),
                selectedYear = LocalDate.now().year,
                editMode = EditMode.EDIT,
                isYearDisabled = false,
                isLoading = false,
                isSaving = false,
                isError = true,
                error = AddEventError.ERROR_LOADING.asConsumableEvent(),
                validYearRange = 1900..2100,
            ),
            onEventNameChange = {},
            onDateSelected = {},
            onYearSelected = {},
            onYearCleared = {},
            onYearDisabled = {},
            onActionClick = {},
            onDeleteClick = {},
            onBottomSheetDismiss = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
//endregion