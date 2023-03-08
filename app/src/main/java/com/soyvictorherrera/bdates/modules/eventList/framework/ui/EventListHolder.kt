package com.soyvictorherrera.bdates.modules.eventList.framework.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.soyvictorherrera.bdates.databinding.ItemEventBinding
import com.soyvictorherrera.bdates.modules.eventList.framework.presentation.EventViewState

class EventListHolder(
    private val binding: ItemEventBinding,
    private val onItemClick: (String) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    private var currentItem: EventViewState? = null

    init {
        binding.root.setOnClickListener {
            currentItem?.id?.let(onItemClick)
        }
    }

    fun render(event: EventViewState) = with(binding) {
        currentItem = event
        lblRemainingTimeValue.text = event.remainingTimeValue
        lblRemainingTimeUnit.text = event.remainingTimeUnit.uppercase()
        lblEventName.text = event.name
        lblEventDesc.text = event.description
        lblEventEmoji.text = "🎂"
    }

    companion object {
        fun inflate(
            parent: ViewGroup,
            onItemClick: (String) -> Unit,
        ): EventListHolder {
            val inflater = LayoutInflater.from(parent.context)
            return EventListHolder(
                binding = ItemEventBinding.inflate(inflater, parent, false),
                onItemClick = onItemClick
            )
        }
    }
}
