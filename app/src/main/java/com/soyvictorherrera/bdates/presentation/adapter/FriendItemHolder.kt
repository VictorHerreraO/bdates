package com.soyvictorherrera.bdates.presentation.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.soyvictorherrera.bdates.R
import com.soyvictorherrera.bdates.databinding.ItemFriendBinding
import com.soyvictorherrera.bdates.domain.model.FriendModel
import com.soyvictorherrera.bdates.utils.extensions.toDayString

class FriendItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = ItemFriendBinding.bind(itemView)

    fun update(friend: FriendModel): Unit = with(binding) {
        lblInitialValue.apply {

            text = friend.name.substring(0, 1)
        }
        lblNameValue.apply {
            text = friend.name
        }
        lblDateValue.apply {
            val date = friend.birthDate ?: friend.stubBirthDate
            text = date?.let {
                itemView.context.getString(
                    R.string.friend_bdate_value_colon,
                    it.toDayString()
                )
            }
        }
        lblAgeValue.apply {
            text = itemView.context.getString(
                R.string.friend_age_value,
                friend.nextYearsOld
            )
            visibility = if (friend.nextYearsOld != FriendModel.YEARS_UNKNOWN) View.VISIBLE
            else View.GONE
        }
    }


}
