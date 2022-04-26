package com.soyvictorherrera.bdates.modules.eventList.framework.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.soyvictorherrera.bdates.R
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event

class EventListAdapter : ListAdapter<Event, EventListHolder>(EventDiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventListHolder {
        return EventListHolder(
            itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.item_event,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: EventListHolder, position: Int) {
        holder.render(getItem(position))
    }
}

object EventDiffUtil : DiffUtil.ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event, newItem: Event) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Event, newItem: Event) = oldItem == newItem
}
