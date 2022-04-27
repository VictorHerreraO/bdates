package com.soyvictorherrera.bdates.modules.eventList.framework.ui

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.soyvictorherrera.bdates.databinding.FragmentEventListBinding
import com.soyvictorherrera.bdates.modules.eventList.framework.presentation.EventListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventListFragment : Fragment() {

    private var _binding: FragmentEventListBinding? = null
    private val binding: FragmentEventListBinding
        get() = _binding!!
    private val viewModel: EventListViewModel by viewModels()

    private lateinit var adapter: EventListAdapter
    private lateinit var todayAdapter: TodayEventListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        initRecyclerView()
        setupListeners()
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
            recyclerTodayEvents.layoutManager = it
        }
        todayAdapter = TodayEventListAdapter().also {
            recyclerTodayEvents.adapter = it
        }
    }

    private fun setupListeners() = with(binding) {
        viewModel.events.observe(viewLifecycleOwner, adapter::submitList)
        viewModel.todayEvents.observe(viewLifecycleOwner) {
            todayAdapter.submitList(it)
            layoutTodayEvents.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
        }

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
