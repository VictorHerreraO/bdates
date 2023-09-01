package com.soyvictorherrera.bdates.modules.eventList.framework.ui.compose

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.soyvictorherrera.bdates.R
import com.soyvictorherrera.bdates.core.compose.layout.BottomSheet
import com.soyvictorherrera.bdates.core.compose.layout.SpacerM
import com.soyvictorherrera.bdates.core.compose.theme.BdatesTheme
import com.soyvictorherrera.bdates.core.compose.theme.LocalSizes
import com.soyvictorherrera.bdates.core.compose.widget.PrimaryActionButton
import com.soyvictorherrera.bdates.modules.eventList.framework.presentation.PreviewEventError
import com.soyvictorherrera.bdates.modules.eventList.framework.presentation.PreviewEventViewState

private const val NO_TITLE = ""
private const val BACKGROUND_ALPHA = 0.25f

//region Content
@Composable
fun PreviewEventSheetContent(
    state: PreviewEventViewState,
    onEditEvent: () -> Unit,
    onBottomSheetDismiss: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) = BottomSheet(
    title = NO_TITLE,
    onBottomSheetDismiss = onBottomSheetDismiss,
    hasActions = state.isEditable,
    actions = {
        PreviewActions(
            enabled = state.isSuccess,
            onEditEvent = onEditEvent
        )
    },
    snackbarHostState = snackbarHostState,
) {
    when {
        state.isLoading -> LoadingPreviewEventSheetContent()

        state.isError -> ErrorPreviewEventSheetContent(
            state = state,
            snackbarHostState = snackbarHostState
        )

        state.isSuccess -> SuccessPreviewEventSheetContent(
            state = state
        )
    }
}

@Composable
private fun LoadingPreviewEventSheetContent(
    modifier: Modifier = Modifier,
) = LoadingSheetContent(modifier = modifier)

@Composable
private fun ErrorPreviewEventSheetContent(
    state: PreviewEventViewState,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    ErrorSheetContent(modifier)

    val context = LocalContext.current
    LaunchedEffect(key1 = state.error) {
        state.error?.showMessage(
            context = context,
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
private fun SuccessPreviewEventSheetContent(
    state: PreviewEventViewState,
    modifier: Modifier = Modifier,
) = Box(
    modifier = modifier.fillMaxWidth(),
    contentAlignment = Alignment.Center
) {
    BackgroundImage()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeadingIcon(
            iconSize = LocalSizes.current.dimen_100
        )

        EventCircleDescription(
            circleName = state.circleName
        )

        SpacerM()

        EventTitle(
            eventName = state.eventName,
            ordinalAge = state.ordinalAge
        )

        SpacerM()

        EventSummary(
            eventDate = state.eventDate,
            remainingDays = state.remainingDays
        )
    }
}
//endregion

//region Components
@Composable
private fun BackgroundImage(
    modifier: Modifier = Modifier,
) = Image(
    painter = painterResource(id = R.drawable.confetti_exact),
    modifier = modifier.fillMaxWidth(),
    alpha = BACKGROUND_ALPHA,
    alignment = Alignment.Center,
    contentScale = ContentScale.Inside,
    contentDescription = null,
)

@Composable
private fun HeadingIcon(
    iconSize: Dp,
    modifier: Modifier = Modifier,
    @DrawableRes iconDrawable: Int = R.drawable.popper,
) = Image(
    painter = painterResource(id = iconDrawable),
    contentDescription = null,
    modifier = modifier.size(iconSize)
)

@Composable
private fun EventTitle(
    eventName: String,
    ordinalAge: String?,
    modifier: Modifier = Modifier,
) = Text(
    text = stringResource(
        id = ordinalAge
            ?.let { R.string.preview_event_title_birthday_w_age }
            ?: R.string.preview_event_title_birthday,
        eventName,
        ordinalAge.orEmpty()
    ),
    style = MaterialTheme.typography.h5,
    fontWeight = FontWeight.Bold,
    textAlign = TextAlign.Center,
    modifier = modifier,
)

@Composable
private fun EventCircleDescription(
    circleName: String?,
    modifier: Modifier = Modifier,
) = circleName?.let { name ->
    SpacerM()

    Text(
        text = name,
        style = MaterialTheme.typography.caption,
        modifier = modifier,
    )
}

@Composable
private fun EventSummary(
    eventDate: String,
    remainingDays: String,
    modifier: Modifier = Modifier,
) {
    val remainingDaysText = pluralStringResource(
        id = R.plurals.preview_event_remaining_time,
        count = remainingDays.toIntOrNull() ?: 0,
        remainingDays
    )
    Text(
        text = stringResource(
            id = R.string.preview_event_date_w_remaining_days_subtitle,
            eventDate,
            remainingDaysText
        ),
        style = MaterialTheme.typography.caption,
        modifier = modifier,
    )
}

@Composable
private fun PreviewActions(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onEditEvent: () -> Unit,
) = PrimaryActionButton(
    text = R.string.add_event_title_edit,
    modifier = modifier.fillMaxWidth(),
    enabled = enabled,
    onClick = onEditEvent
)
//endregion

//region Error messages
private suspend fun PreviewEventError.showMessage(
    context: Context,
    snackbarHostState: SnackbarHostState,
) {
    when (this) {
        PreviewEventError.ERROR_LOADING -> snackbarHostState.showSnackbar(
            message = context.getString(R.string.preview_event_error_loading),
            duration = SnackbarDuration.Indefinite,
        )
    }
}
//endregion

//region Previews
@Preview(showSystemUi = true)
@Composable
private fun PreviewEventSheetContentPreview() {
    BdatesTheme(darkTheme = true) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            val state = PreviewEventViewState(
                ordinalAge = null,
                circleName = "Family",
                eventName = "Michael Scott",
                eventDate = "Monday, 03/15",
                eventType = "Birthday",
                isEditable = false,
                isLoading = false,
                remainingDays = "10"
            )
            PreviewEventSheetContent(
                state = state,
                onEditEvent = {},
                onBottomSheetDismiss = {},
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun EditablePreviewEventSheetContentPreview() {
    BdatesTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            val state = PreviewEventViewState(
                ordinalAge = "41",
                eventName = "Michael Scott",
                eventDate = "Monday, 03/15",
                eventType = "Birthday",
                isEditable = true,
                isLoading = false,
                remainingDays = "1"
            )
            PreviewEventSheetContent(
                state = state,
                onEditEvent = {},
                onBottomSheetDismiss = {},
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LoadingPreviewEventSheetContentPreview() {
    BdatesTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            val state = PreviewEventViewState(
                ordinalAge = "",
                eventName = "",
                eventDate = "",
                eventType = "",
                isEditable = false,
                isLoading = true,
                remainingDays = "",
            )
            PreviewEventSheetContent(
                state = state,
                onEditEvent = {},
                onBottomSheetDismiss = {},
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LoadingEditablePreviewEventSheetContentPreview() {
    BdatesTheme(darkTheme = true) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            val state = PreviewEventViewState(
                ordinalAge = "",
                eventName = "",
                eventDate = "",
                eventType = "",
                isEditable = true,
                isLoading = true,
                remainingDays = "",
            )
            PreviewEventSheetContent(
                state = state,
                onEditEvent = {},
                onBottomSheetDismiss = {},
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ErrorPreviewEventSheetContentPreview() {
    BdatesTheme(darkTheme = true) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            val state = PreviewEventViewState(
                ordinalAge = "",
                eventName = "",
                eventDate = "",
                eventType = "",
                isEditable = false,
                isLoading = false,
                isError = true,
                error = PreviewEventError.ERROR_LOADING,
                remainingDays = "",
            )
            PreviewEventSheetContent(
                state = state,
                onEditEvent = {},
                onBottomSheetDismiss = {},
            )
        }
    }
}
//endregion
