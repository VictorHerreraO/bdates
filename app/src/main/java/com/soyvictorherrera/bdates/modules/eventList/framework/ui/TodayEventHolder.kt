package com.soyvictorherrera.bdates.modules.eventList.framework.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.soyvictorherrera.bdates.databinding.ItemEventBirthDayBinding
import com.soyvictorherrera.bdates.modules.eventList.framework.presentation.TodayEventViewState

class TodayEventHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = ItemEventBirthDayBinding.bind(itemView)

    fun render(event: TodayEventViewState) = with(binding) {
        lblAgeValue.text = event.friendAge
        lblAgeValue.visibility = if (event.friendAge.isNullOrEmpty()) View.GONE else View.VISIBLE
        lblFriendName.text = event.friendName
        lblEventType.text = event.eventType.uppercase()
    }

}
