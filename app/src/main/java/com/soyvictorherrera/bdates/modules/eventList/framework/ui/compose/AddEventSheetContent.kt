package com.soyvictorherrera.bdates.modules.eventList.framework.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.soyvictorherrera.bdates.core.compose.layout.BottomSheet
import com.soyvictorherrera.bdates.core.compose.layout.SpacerM
import com.soyvictorherrera.bdates.core.compose.layout.SpacerSm
import com.soyvictorherrera.bdates.core.compose.theme.BdatesTheme
import java.time.LocalDate

internal const val ICON_ALPHA = 0.6F

@Composable
fun AddEventSheetContent(
    modifier: Modifier = Modifier,
) = BottomSheet(
    modifier = modifier
) {
    Column {
        Text(text = "Event name:", style = MaterialTheme.typography.caption)

        SpacerSm()

        val hi = remember { mutableStateOf("Hi!") }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = hi.value,
            onValueChange = { hi.value = it }
        )

        SpacerM()

        Text(text = "Event date:", style = MaterialTheme.typography.caption)

        SpacerSm()

        var selectedDate by remember { mutableStateOf(LocalDate.now()) }
        DateSelector(
            selectedDate = selectedDate,
            onDateSelected = { selectedDate = it },
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