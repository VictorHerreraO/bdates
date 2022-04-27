package com.soyvictorherrera.bdates.modules.eventList.framework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        LinearLayoutManager(requireActivity()).also {
            recyclerEvents.layoutManager = it
        }
        adapter = EventListAdapter().also {
            recyclerEvents.adapter = it
        }
        LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false).also {
            recyclerTodayEvents.layoutManager = it
        }
        todayAdapter = TodayEventListAdapter().also {
            recyclerTodayEvents.adapter = it
        }
    }

    private fun setupListeners() {
        viewModel.events.observe(viewLifecycleOwner, adapter::submitList)
        viewModel.todayEvents.observe(viewLifecycleOwner) {
            todayAdapter.submitList(it)
            binding.layoutTodayEvents.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
        }
    }

}
