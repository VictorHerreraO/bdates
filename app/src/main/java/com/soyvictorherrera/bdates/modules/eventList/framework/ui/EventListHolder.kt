package com.soyvictorherrera.bdates.modules.eventList.framework.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.soyvictorherrera.bdates.databinding.ItemEventBinding
import com.soyvictorherrera.bdates.modules.eventList.framework.presentation.EventViewState

class EventListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = ItemEventBinding.bind(itemView)

    fun render(event: EventViewState) = with(binding) {
        lblRemainingTimeValue.text = event.remainingTimeValue
        lblRemainingTimeUnit.text = event.remainingTimeUnit.uppercase()
        lblEventName.text = event.name
        lblEventDesc.text = event.description
        lblEventEmoji.text = "🎂"
    }

}
