package com.malek.giffy.ui.recyclerview

import androidx.databinding.ViewDataBinding

import androidx.recyclerview.widget.RecyclerView
import com.malek.giffy.BR


class BaseViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(
    binding.root
) {
    fun bind(baseViewModelItem: BaseViewModelItem) {
        binding.setVariable(BR.model, baseViewModelItem)
        binding.executePendingBindings()
    }
}