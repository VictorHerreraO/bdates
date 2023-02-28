package com.soyvictorherrera.bdates.modules.eventList.framework.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.soyvictorherrera.bdates.core.compose.theme.setBdatesContent
import com.soyvictorherrera.bdates.modules.eventList.framework.presentation.AddEventViewModel
import com.soyvictorherrera.bdates.modules.eventList.framework.ui.compose.AddEventSheetContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEventBottomSheet : BottomSheetDialogFragment() {
    private val viewModel: AddEventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setBdatesContent {
            val state by viewModel.state.collectAsState()
            Column {
                AddEventSheetContent(
                    state = state,
                    onEventNameChange = viewModel::onEventNameChange,
                    onDateSelected = viewModel::onDateSelected,
                    onYearDisabled = viewModel::onYearDisabled,
                    onActionClick = viewModel::onActionClick,
                    onBottomSheetDismiss = { dismiss() }
                )
            }
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener {
                (view?.parent as ViewGroup).background =
                    ColorDrawable(Color.TRANSPARENT)
            }
        }
    }
}