package com.soyvictorherrera.bdates.modules.eventList.framework.ui

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.soyvictorherrera.bdates.NavGraphDirections
import com.soyvictorherrera.bdates.R
import com.soyvictorherrera.bdates.core.event.NavigationEvent
import com.soyvictorherrera.bdates.core.event.consume
import com.soyvictorherrera.bdates.core.event.consumeValue
import com.soyvictorherrera.bdates.databinding.FragmentEventListBinding
import com.soyvictorherrera.bdates.modules.eventList.framework.presentation.Error
import com.soyvictorherrera.bdates.modules.eventList.framework.presentation.EventListViewModel
import com.soyvictorherrera.bdates.modules.permissions.PermissionDelegate
import com.soyvictorherrera.bdates.modules.permissions.PermissionDelegateFactory
import com.soyvictorherrera.bdates.modules.permissions.isPostNotificationPermissionGranted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventListFragment : Fragment() {

    private var _binding: FragmentEventListBinding? = null
    private val binding: FragmentEventListBinding
        get() = _binding!!

    private val viewModel: EventListViewModel by viewModels()

    private lateinit var adapter: EventListAdapter
    private lateinit var permissionDelegate: PermissionDelegate
    private lateinit var todayAdapter: TodayEventListAdapter

    private var onScrollListener: RecyclerView.OnScrollListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentEventListBinding.inflate(inflater, container, false)
        permissionDelegate = PermissionDelegateFactory.create { requireActivity() }
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        initRecyclerView()
        bindViewModel()
        setupListeners()
        setupResultListener()
        bindViewModel()
    }

    override fun onDestroyView() {
        _binding = null
        onScrollListener = null
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onNotificationPermissionStateCheck(
            requireContext().isPostNotificationPermissionGranted
        )
    }

    private fun initRecyclerView() = with(binding) {
        val orientation = resources.configuration.orientation
        // Setup all events recycler view
        LinearLayoutManager(requireActivity()).also {
            layoutUpcomingEvents.recyclerEvents.layoutManager = it
        }
        adapter = EventListAdapter(onItemClick = {
            viewModel.onEventClick(it)
        }).also {
            layoutUpcomingEvents.recyclerEvents.adapter = it
        }
        onScrollListener = FabScrollBehavior(btnAddEvent).also {
            layoutUpcomingEvents.recyclerEvents.addOnScrollListener(it)
        }
        // Setup today events recycler view
        LinearLayoutManager(
            requireContext(),
            if (orientation == Configuration.ORIENTATION_PORTRAIT) RecyclerView.HORIZONTAL
            else RecyclerView.VERTICAL,
            false
        ).also {
            recyclerTodayEvents.apply {
                layoutManager = it
                val decoration = DividerItemDecoration(requireContext(), it.orientation)
                decoration.setDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.item_decorator_day_events
                    )!!
                )
                addItemDecoration(decoration)
            }
        }
        todayAdapter = TodayEventListAdapter().also {
            recyclerTodayEvents.adapter = it
        }
    }

    private fun bindViewModel() = with(viewModel) {
        events.observe(viewLifecycleOwner, adapter::submitList)
        todayEvents.observe(viewLifecycleOwner) { todayEvents ->
            todayAdapter.submitList(todayEvents)
            binding.layoutTodayEvents.isVisible = todayEvents.isNotEmpty()
        }
        requestPermissionSignal.observe(viewLifecycleOwner) { shouldRequestPermission ->
            if (shouldRequestPermission) {
                permissionDelegate.requestNotificationPermission(
                    viewModel::onNotificationPermissionStateChanged
                )
            }
        }
        showMissingPermissionMessage.observe(viewLifecycleOwner) { showMessage ->
            binding.layoutUpcomingEvents.layoutWarningBanner.root.isVisible = showMessage
        }
        navigation.observe(viewLifecycleOwner) { consumable ->
            consumable.consume { event ->
                when (event) {
                    is NavigationEvent.AddEventBottomSheet -> {
                        NavGraphDirections.actionCreateEventBottomSheet(
                            eventId = event.eventId
                        ).run {
                            findNavController().navigate(this)
                        }
                    }

                    is NavigationEvent.PreviewEventBottomSheet -> {
                        NavGraphDirections.actionPreviewEventBottomSheet(
                            eventId = event.eventId
                        ).run {
                            findNavController().navigate(this)
                        }
                    }

                    is NavigationEvent.NavigateBack -> {
                        /* Do nothing */
                    }
                }
            }
        }
        events.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            with(binding.layoutUpcomingEvents) {
                val showEmptyIcon = it.isEmpty() && inputSearch.text.isEmpty()
                lblUpcomingEventsTitle.isGone = showEmptyIcon
                swipeLayout.isGone = showEmptyIcon
                inputSearch.isGone = showEmptyIcon
                layoutEventListEmpty.root.isVisible = showEmptyIcon
            }
        }
        todayEvents.observe(viewLifecycleOwner) {
            todayAdapter.submitList(it)
            binding.layoutTodayEvents.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
        }
        isRefreshing.observe(viewLifecycleOwner) {
            binding.layoutUpcomingEvents.swipeLayout.isRefreshing = it
        }
        errorMessage.observe(viewLifecycleOwner) {
            it.consumeValue(::showSnackBar)
        }
    }

    private fun setupListeners() = with(binding) {
        layoutUpcomingEvents.inputSearch.addTextChangedListener { text ->
            viewModel.onQueryTextChanged(text.toString())
        }
        layoutUpcomingEvents.inputSearch.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    hideSoftKeyboard()
                    true
                }

                else -> false
            }
        }
        btnAddEvent.setOnClickListener {
            viewModel.onAddEventClick()
        }
        layoutUpcomingEvents.swipeLayout.setOnRefreshListener {
            viewModel.refresh()
        }
        layoutUpcomingEvents.layoutWarningBanner.root.setOnClickListener {
            openAppSettings()
        }
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", requireContext().packageName, null)
        }
        startActivity(intent)
    }

    private fun hideSoftKeyboard() {
        activity?.let {
            it.currentFocus?.let { view ->
                (it.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    private fun setupResultListener() {
        setFragmentResultListener(REQUEST_KEY_ADD_EVENT) { _, bundle ->
            bundle.getBoolean(RESULT_KEY_ADD_EVENT).let { created ->
                if (created) {
                    viewModel.refresh()
                }
            }
        }
    }

    private fun showSnackBar(error: Error): Unit = with(binding) {
        Snackbar.make(
            layoutRoot,
            error.getMessage(),
            Snackbar.LENGTH_SHORT
        ).apply {
            anchorView = btnAddEvent
        }.show()
    }

}

@StringRes
private fun Error.getMessage(): Int = when (this) {
    Error.UnableToRefresh -> R.string.event_list_error_fetch_list
}
