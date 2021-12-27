package com.soyvictorherrera.bdates.presentation.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.soyvictorherrera.bdates.databinding.ItemTemplateBinding

class TemplateHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = ItemTemplateBinding.bind(itemView)

    fun update(template: String): Unit = with(binding) {
        lblTemplateContent.apply {
            text = template
        }
    }

}
