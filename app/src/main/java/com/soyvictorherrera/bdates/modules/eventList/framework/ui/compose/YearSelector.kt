package com.soyvictorherrera.bdates.modules.eventList.framework.ui.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.soyvictorherrera.bdates.core.compose.theme.BdatesTheme
import com.soyvictorherrera.bdates.core.compose.theme.Shapes
import java.time.LocalDate

private val VALIDATION_REGEX = Regex("\\d+")

@Composable
fun YearSelector(
    selectedYear: Int?,
    onYearSelected: (Int) -> Unit,
    onYearCleared: () -> Unit,
    validYearRange: IntRange,
    modifier: Modifier = Modifier,
    border: BorderStroke? = null,
    elevation: Dp = 0.dp,
    enabled: Boolean = true,
) = Surface(
    modifier = modifier,
    shape = Shapes.medium,
    border = border,
    elevation = elevation,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextField(
            value = selectedYear?.toString().orEmpty(),
            onValueChange = { text ->
                text.takeIf { it.matches(VALIDATION_REGEX) }
                    ?.toIntOrNull()
                    ?.let {
                        onYearSelected(it)
                    } ?: onYearCleared()
            },
            modifier = Modifier.weight(1f),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            enabled = enabled,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = MaterialTheme.colors.secondary,
                focusedIndicatorColor = MaterialTheme.colors.secondary
            ),
            leadingIcon = {
                val prevYearEnabled = enabled && selectedYear in validYearRange
                IconButton(
                    enabled = prevYearEnabled,
                    onClick = { selectedYear?.dec()?.let(onYearSelected) }
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Add",
                        tint = MaterialTheme.colors.onSurface.copy(
                            alpha = if (prevYearEnabled) {
                                ICON_ALPHA
                            } else {
                                ICON_DISABLED_ALPHA
                            }
                        ),
                    )
                }
            },
            trailingIcon = {
                val nextYearEnabled = enabled && selectedYear?.inc() in validYearRange
                IconButton(
                    enabled = nextYearEnabled,
                    onClick = { selectedYear?.inc()?.let(onYearSelected) }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowRight,
                        contentDescription = "Add",
                        tint = MaterialTheme.colors.onSurface.copy(
                            alpha = if (nextYearEnabled) {
                                ICON_ALPHA
                            } else {
                                ICON_DISABLED_ALPHA
                            }
                        ),
                    )
                }
            },
        )
    }
}

@Composable
@Preview(showBackground = true)
fun YearSelectorPreviewLight() {
    BdatesTheme(darkTheme = false) {
        var selectedDate: Int? by remember { mutableStateOf(LocalDate.now().year) }
        YearSelector(
            selectedYear = selectedDate,
            onYearSelected = { selectedDate = it },
            onYearCleared = { selectedDate = null },
            validYearRange = 1900..2100,
        )
    }
}

@Composable
@Preview(showBackground = true)
fun YearSelectorPreviewDark() {
    BdatesTheme(darkTheme = true) {
        var selectedDate: Int? by remember { mutableStateOf(LocalDate.now().year) }
        YearSelector(
            selectedYear = selectedDate,
            onYearSelected = { selectedDate = it },
            onYearCleared = { selectedDate = null },
            validYearRange = 1900..2100,
        )
    }
}