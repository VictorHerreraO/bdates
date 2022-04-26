package com.soyvictorherrera.bdates.modules.eventList.framework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
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
    }

    private fun setupListeners() {
        viewModel.events.observe(viewLifecycleOwner, adapter::submitList)
    }

}
