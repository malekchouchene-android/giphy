package com.malek.giffy.ui.search

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.malek.giffy.R
import com.malek.giffy.domaine.GIF

class GifListAdapter : RecyclerView.Adapter<GifListAdapter.GIFViewHolder>() {
    private val dataSet = mutableListOf<GIF>()

    class GIFViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val preview = view.findViewById<ImageView>(R.id.preview_item_list)
         fun bind(gif: GIF) {
            Glide.with(preview.context)
                    .asGif()
                    .load(gif.preview)
                    .placeholder(ColorDrawable((Math.random() * 16777215).toInt() or (0xFF shl 24)))
                    .into(preview)


        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GIFViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.gif_item_layout, parent, false)
        return GIFViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: GIFViewHolder, position: Int) {
        holder.bind(gif = dataSet[position])
    }

    fun updateData(newData: List<GIF>) {
        dataSet.clear()
        dataSet.addAll(newData)
        this.notifyDataSetChanged()
    }
}
