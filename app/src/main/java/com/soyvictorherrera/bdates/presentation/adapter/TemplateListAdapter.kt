package com.soyvictorherrera.bdates.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.soyvictorherrera.bdates.R

class TemplateListAdapter : ListAdapter<String, TemplateHolder>(StringDiffUtil) {

    var onItemClickListener: ((template: String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemplateHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_template,
            parent,
            false
        )
        return TemplateHolder(itemView).apply {
            itemView.setOnClickListener {
                onItemClickListener?.invoke(getItem(adapterPosition))
            }
        }
    }

    override fun onBindViewHolder(holder: TemplateHolder, position: Int) {
        holder.update(getItem(position))
    }
}

object StringDiffUtil : DiffUtil.ItemCallback<String>() {
    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}