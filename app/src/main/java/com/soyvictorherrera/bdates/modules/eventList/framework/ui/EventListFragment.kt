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
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.soyvictorherrera.bdates.R
import com.soyvictorherrera.bdates.databinding.FragmentEventListBinding
import com.soyvictorherrera.bdates.modules.eventList.framework.presentation.EventListViewModel
import com.soyvictorherrera.bdates.modules.permissions.PermissionDelegate
import com.soyvictorherrera.bdates.modules.permissions.PermissionDelegateFactory
import com.soyvictorherrera.bdates.modules.permissions.isPostNotificationPermissionGranted
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class EventListFragment : Fragment() {

    private var _binding: FragmentEventListBinding? = null
    private val binding: FragmentEventListBinding
        get() = _binding!!

    private val viewModel: EventListViewModel by viewModels()

    private lateinit var adapter: EventListAdapter
    private lateinit var permissionDelegate: PermissionDelegate
    private lateinit var todayAdapter: TodayEventListAdapter

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
    }

    override fun onResume() {
        super.onResume()
        viewModel.onNotificationPermissionStateCheck(
            requireContext().isPostNotificationPermissionGranted
        )
    }

    private fun initRecyclerView() = with(binding) {
        val orientation = resources.configuration.orientation
        LinearLayoutManager(requireActivity()).also {
            recyclerEvents.layoutManager = it
        }
        adapter = EventListAdapter().also {
            recyclerEvents.adapter = it
        }
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
            Timber.d("Should request permission? $shouldRequestPermission")
            if (shouldRequestPermission) {
                permissionDelegate.requestNotificationPermission(
                    viewModel::onNotificationPermissionStateChanged
                )
            }
        }
        showMissingPermissionMessage.observe(viewLifecycleOwner) { showMessage ->
            binding.layoutWarningBanner.root.isVisible = showMessage
        }
    }

    private fun setupListeners() = with(binding) {
        inputSearch.addTextChangedListener { text ->
            viewModel.onQueryTextChanged(text.toString())
        }
        inputSearch.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    hideSoftKeyboard()
                    true
                }

                else -> false
            }
        }
        layoutWarningBanner.root.setOnClickListener {
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

}
