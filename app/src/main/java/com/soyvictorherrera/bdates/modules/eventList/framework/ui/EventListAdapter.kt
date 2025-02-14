package com.soyvictorherrera.bdates.modules.eventList.framework.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.soyvictorherrera.bdates.modules.eventList.framework.presentation.EventViewState

class EventListAdapter(
    private val onItemClick: (String) -> Unit,
) : ListAdapter<EventViewState, EventListHolder>(EventDiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventListHolder {
        return EventListHolder.inflate(
            parent = parent,
            onItemClick = onItemClick
        )
    }

    override fun onBindViewHolder(holder: EventListHolder, position: Int) {
        holder.render(getItem(position))
    }
}

object EventDiffUtil : DiffUtil.ItemCallback<EventViewState>() {
    override fun areItemsTheSame(oldItem: EventViewState, newItem: EventViewState): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: EventViewState, newItem: EventViewState): Boolean {
        return oldItem == newItem
    }
}
