package com.malek.giffy.ui.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.malek.giffy.domaine.GIF


class DataBindingAdapter : RecyclerView.Adapter<BaseViewHolder>() {
    private val dataSet = mutableListOf<BaseViewModelItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            layoutInflater, viewType, parent, false
        )
        return BaseViewHolder(binding = binding)
    }

    override fun getItemViewType(position: Int): Int {
        return dataSet[position].layout
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(baseViewModelItem = dataSet[position])
    }

    fun updateData(modelItems: List<BaseViewModelItem>) {
        dataSet.clear()
        dataSet.addAll(modelItems)
        notifyDataSetChanged()
    }
}


@BindingAdapter("models")
fun updateDataRecyclerView(recyclerView: RecyclerView, modelItems: List<BaseViewModelItem>?) {
    modelItems?.let {
        if (recyclerView.adapter == null) {
            recyclerView.adapter = DataBindingAdapter()
        }
        (recyclerView.adapter as? DataBindingAdapter)?.updateData(modelItems)
    }

}