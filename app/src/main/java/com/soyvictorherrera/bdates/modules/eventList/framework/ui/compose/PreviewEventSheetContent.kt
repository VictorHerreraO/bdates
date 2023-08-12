package com.soyvictorherrera.bdates.modules.eventList.framework.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.soyvictorherrera.bdates.R
import com.soyvictorherrera.bdates.core.compose.layout.BottomSheet
import com.soyvictorherrera.bdates.core.compose.layout.SpacerM
import com.soyvictorherrera.bdates.core.compose.theme.BdatesTheme
import com.soyvictorherrera.bdates.core.compose.theme.LocalSizes
import com.soyvictorherrera.bdates.core.compose.theme.Rajah
import com.soyvictorherrera.bdates.core.compose.theme.Typography
import com.soyvictorherrera.bdates.core.compose.widget.PrimaryActionButton
import com.soyvictorherrera.bdates.modules.eventList.framework.presentation.PreviewEventViewState

private const val NO_TITLE = ""
private const val BACKGROUND_ALPHA = 0.25f

//region Content
@Composable
fun PreviewEventSheetContent(
    state: PreviewEventViewState,
    onEditEvent: () -> Unit,
    onBottomSheetDismiss: () -> Unit,
) = BottomSheet(
    title = NO_TITLE,
    onBottomSheetDismiss = onBottomSheetDismiss,
    hasActions = state.isEditable,
    actions = {
        PreviewActions(onEditEvent = onEditEvent)
    },
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        BackgroundImage()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MainEventHeading(text = state.age)

            state.circleName?.let { circleName ->
                SpacerM()

                Text(
                    text = circleName,
                    style = MaterialTheme.typography.caption
                )
            }

            SpacerM()

            Text(
                text = state.eventName,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            SpacerM()

            Text(
                text = stringResource(
                    id = R.string.preview_event_date_type_subtitle,
                    state.eventDate,
                    state.eventType
                ),
                style = MaterialTheme.typography.caption
            )
        }
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
private fun MainEventHeading(
    text: String?,
    modifier: Modifier = Modifier,
) = if (text != null) {
    Text(
        text = text,
        style = BalloonTextStyle
    )
} else {
    Image(
        painter = painterResource(id = R.drawable.popper),
        contentDescription = null,
        modifier = modifier.size(LocalSizes.current.dimen_100)
    )
}

@Composable
private fun PreviewActions(
    modifier: Modifier = Modifier,
    onEditEvent: () -> Unit,
) = PrimaryActionButton(
    text = R.string.add_event_title_edit,
    modifier = modifier.fillMaxWidth(),
    onClick = onEditEvent
)

private val BalloonTextStyle: TextStyle
    @Composable
    get() = Typography.h1.copy(
        color = if (isSystemInDarkTheme()) {
            Rajah
        } else {
            Rajah
        },
        fontWeight = FontWeight.Bold,
    )
//endregion

//region Previews
@Preview(showSystemUi = true)
@Composable
private fun EventPreviewContentPreview() {
    BdatesTheme(darkTheme = true) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            val state = PreviewEventViewState(
                age = null,
                circleName = "Family",
                eventName = "Michael Scott",
                eventDate = "Monday, 03/15",
                eventType = "Birthday",
                isEditable = false,
                isLoading = false,
            )
            PreviewEventSheetContent(
                state = state,
                onEditEvent = {},
                onBottomSheetDismiss = {}
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun EditableEventPreviewContentPreview() {
    BdatesTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            val state = PreviewEventViewState(
                age = "41",
                eventName = "Michael Scott",
                eventDate = "Monday, 03/15",
                eventType = "Birthday",
                isEditable = true,
                isLoading = false
            )
            PreviewEventSheetContent(
                state = state,
                onEditEvent = {},
                onBottomSheetDismiss = {}
            )
        }
    }
}
//endregion
