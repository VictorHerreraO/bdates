package com.soyvictorherrera.bdates.modules.eventList.framework.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.soyvictorherrera.bdates.R
import com.soyvictorherrera.bdates.modules.eventList.framework.presentation.TodayEventViewState

class TodayEventListAdapter :
    ListAdapter<TodayEventViewState, TodayEventHolder>(TodayEventDiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayEventHolder {
        return TodayEventHolder(
            itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.item_event_birth_day,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TodayEventHolder, position: Int) {
        holder.render(getItem(position))
    }
}

object TodayEventDiffUtil : DiffUtil.ItemCallback<TodayEventViewState>() {
    override fun areItemsTheSame(
        oldItem: TodayEventViewState,
        newItem: TodayEventViewState
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: TodayEventViewState,
        newItem: TodayEventViewState
    ): Boolean {
        return oldItem == newItem
    }
}
