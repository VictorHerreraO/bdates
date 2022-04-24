package com.soyvictorherrera.bdates.modules.eventList.framework.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.soyvictorherrera.bdates.modules.eventList.framework.presentation.EventListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventListFragment : Fragment() {

    private val viewModel: EventListViewModel by viewModels()

}
