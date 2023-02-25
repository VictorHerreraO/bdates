package com.soyvictorherrera.bdates.modules.eventList.framework.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.soyvictorherrera.bdates.core.compose.theme.BdatesTheme
import com.soyvictorherrera.bdates.core.compose.theme.LocalSizes

@Composable
fun AddEventSheetContent(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(LocalSizes.current.dimen_16)
    ) {
        val hi = remember { mutableStateOf("Hi!") }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = hi.value,
            onValueChange = { hi.value = it }
        )
        Spacer(modifier = Modifier.height(LocalSizes.current.dimen_8))
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