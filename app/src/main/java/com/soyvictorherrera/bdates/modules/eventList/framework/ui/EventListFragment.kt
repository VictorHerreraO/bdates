package com.soyvictorherrera.bdates.modules.eventList.framework.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.soyvictorherrera.bdates.NavGraphDirections
import com.soyvictorherrera.bdates.core.compose.theme.BdatesTheme
import com.soyvictorherrera.bdates.core.event.NavigationEvent
import com.soyvictorherrera.bdates.core.event.consume
import com.soyvictorherrera.bdates.modules.eventList.framework.presentation.EventListAction
import com.soyvictorherrera.bdates.modules.eventList.framework.presentation.EventListViewModel
import com.soyvictorherrera.bdates.modules.eventList.framework.ui.compose.EventListScreen
import com.soyvictorherrera.bdates.modules.permissions.PermissionDelegate
import com.soyvictorherrera.bdates.modules.permissions.PermissionDelegateFactory
import com.soyvictorherrera.bdates.modules.permissions.isPostNotificationPermissionGranted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@AndroidEntryPoint
class EventListFragment : Fragment() {

    private val viewModel: EventListViewModel by viewModels()
    private lateinit var permissionDelegate: PermissionDelegate

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        permissionDelegate = PermissionDelegateFactory.create { requireActivity() }
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                BdatesTheme {
                    val state by viewModel.uiState.collectAsStateWithLifecycle()
                    EventListScreen(
                        state = state,
                        onAction = { action ->
                            when (action) {
                                is EventListAction.OpenAppSettings -> openAppSettings()
                                else -> viewModel.onAction(action)
                            }
                        },
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupResultListener()
        observeNavigation()
        observePermissionSignal()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onAction(
            EventListAction.NotificationPermissionStateCheck(
                requireContext().isPostNotificationPermissionGranted
            )
        )
    }

    private fun observeNavigation() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigation.collect { event ->
                    event?.consume {
                        when (it) {
                            is NavigationEvent.AddEventBottomSheet -> {
                                NavGraphDirections.actionCreateEventBottomSheet(
                                    eventId = it.eventId
                                ).let { directions ->
                                    findNavController().navigate(directions)
                                }
                            }
                            is NavigationEvent.PreviewEventBottomSheet -> {
                                NavGraphDirections.actionPreviewEventBottomSheet(
                                    eventId = it.eventId
                                ).let { directions ->
                                    findNavController().navigate(directions)
                                }
                            }
                            is NavigationEvent.NavigateBack -> {
                                findNavController().popBackStack()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun observePermissionSignal() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .map { it.requestPermission }
                    .distinctUntilChanged()
                    .collect { shouldRequest ->
                        if (shouldRequest) {
                            permissionDelegate.requestNotificationPermission { isGranted ->
                                viewModel.onAction(
                                    EventListAction.NotificationPermissionStateChanged(isGranted)
                                )
                            }
                        }
                    }
            }
        }
    }

    private fun setupResultListener() {
        setFragmentResultListener(REQUEST_KEY_ADD_EVENT) { _, bundle ->
            bundle.getBoolean(RESULT_KEY_ADD_EVENT).let { created ->
                if (created) {
                    viewModel.onAction(EventListAction.Refresh)
                }
            }
        }
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", requireContext().packageName, null)
        }
        startActivity(intent)
    }
}
