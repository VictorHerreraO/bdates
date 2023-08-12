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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.soyvictorherrera.bdates.NavGraphDirections
import com.soyvictorherrera.bdates.R
import com.soyvictorherrera.bdates.core.compose.theme.setBdatesContent
import com.soyvictorherrera.bdates.core.event.NavigationEvent
import com.soyvictorherrera.bdates.core.event.consume
import com.soyvictorherrera.bdates.modules.eventList.framework.presentation.PreviewEventViewModel
import com.soyvictorherrera.bdates.modules.eventList.framework.ui.compose.PreviewEventSheetContent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PreviewEventBottomSheet : BottomSheetDialogFragment() {
    private val viewModel: PreviewEventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setBdatesContent {
            val state by viewModel.state.collectAsState()
            PreviewEventSheetContent(
                state = state,
                onEditEvent = viewModel::onActionClick,
                onBottomSheetDismiss = { dismiss() },
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.navigation.collect { consumable ->
                consumable.consume { event ->
                    when (event) {
                        is NavigationEvent.NavigateBack -> {
                            dismiss()
                        }

                        is NavigationEvent.AddEventBottomSheet -> {
                            NavGraphDirections.actionCreateEventBottomSheet(
                                eventId = event.eventId
                            ).run {
                                val navOptions = navOptions {
                                    popUpTo(R.id.previewEventBottomSheet) {
                                        inclusive = true
                                    }
                                }
                                findNavController().navigate(this, navOptions)
                            }
                        }

                        is NavigationEvent.PreviewEventBottomSheet -> {
                            /* No op */
                        }
                    }
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // TODO: move this logic to a base class
        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener {
                (view?.parent as ViewGroup).background =
                    ColorDrawable(Color.TRANSPARENT)
            }
        }
    }
}