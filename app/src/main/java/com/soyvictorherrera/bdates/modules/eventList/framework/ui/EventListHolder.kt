package com.soyvictorherrera.bdates.modules.eventList.framework.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.soyvictorherrera.bdates.databinding.ItemEventBinding
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event

class EventListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = ItemEventBinding.bind(itemView)

    fun render(event: Event) = with(binding) {
        lblEventName.text = event.name
    }

}
