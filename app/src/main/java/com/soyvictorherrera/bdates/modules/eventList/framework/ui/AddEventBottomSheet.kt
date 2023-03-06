package com.soyvictorherrera.bdates.modules.eventList.framework.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.soyvictorherrera.bdates.core.compose.theme.setBdatesContent
import com.soyvictorherrera.bdates.core.navigation.NavigationEvent
import com.soyvictorherrera.bdates.core.navigation.consume
import com.soyvictorherrera.bdates.modules.eventList.framework.presentation.AddEventViewModel
import com.soyvictorherrera.bdates.modules.eventList.framework.ui.compose.AddEventSheetContent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddEventBottomSheet : BottomSheetDialogFragment() {
    private val viewModel: AddEventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setBdatesContent {
            val state by viewModel.state.collectAsState()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.navigation.collect {
                it.consume { event ->
                    when (event) {
                        is NavigationEvent.NavigateBack -> {
                            notifyEventCreated()
                            dismiss()
                        }
                    }
                }
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

    private fun notifyEventCreated() {
        setFragmentResult(REQUEST_KEY_ADD_EVENT, Bundle().apply {
            putBoolean(RESULT_KEY_ADD_EVENT, true)
        })
    }
}