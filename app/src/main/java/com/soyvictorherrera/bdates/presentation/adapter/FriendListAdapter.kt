package com.soyvictorherrera.bdates.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.soyvictorherrera.bdates.R
import com.soyvictorherrera.bdates.domain.model.FriendModel

class FriendListAdapter : ListAdapter<FriendModel, FriendItemHolder>(FriendDiffUtil) {

    var onItemClickListener: ((friend: FriendModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendItemHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_friend,
            parent,
            false
        )
        return FriendItemHolder(itemView).apply {
            itemView.setOnClickListener {
                onItemClickListener?.invoke(
                    getItem(this.adapterPosition)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: FriendItemHolder, position: Int) {
        getItem(position)?.let {
            holder.update(it)
        }
    }

}

object FriendDiffUtil : DiffUtil.ItemCallback<FriendModel>() {
    override fun areItemsTheSame(oldItem: FriendModel, newItem: FriendModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FriendModel, newItem: FriendModel): Boolean {
        return oldItem == newItem
    }
}
