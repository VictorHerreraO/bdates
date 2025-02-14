package com.soyvictorherrera.bdates.core.compose.layout

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.soyvictorherrera.bdates.core.compose.theme.LocalSizes
import com.soyvictorherrera.bdates.core.compose.theme.Size

/**
 * [Spacer] with size of local [Size.dimen_4]
 */
@Composable
fun SpacerXs() = Spacer(modifier = Modifier.size(LocalSizes.current.dimen_4))

/**
 * [Spacer] with size of local [Size.dimen_8]
 */
@Composable
fun SpacerSm() = Spacer(modifier = Modifier.size(LocalSizes.current.dimen_8))

/**
 * [Spacer] with size of local [Size.dimen_16]
 */
@Composable
fun SpacerM() = Spacer(modifier = Modifier.size(LocalSizes.current.dimen_16))

/**
 * [Spacer] with size of local [Size.dimen_24]
 */
@Composable
fun SpacerL() = Spacer(modifier = Modifier.size(LocalSizes.current.dimen_24))

/**
 * [Spacer] with size of local [Size.dimen_32]
 */
@Composable
fun SpacerXl() = Spacer(modifier = Modifier.size(LocalSizes.current.dimen_32))

/**
 * [Spacer] with size of local [Size.dimen_40]
 */
@Composable
fun SpacerXxl() = Spacer(modifier = Modifier.size(LocalSizes.current.dimen_40))