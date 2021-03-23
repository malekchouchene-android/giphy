package com.malek.giffy.ui.search

import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.navigation.findNavController
import com.malek.giffy.R
import com.malek.giffy.domaine.GIF
import com.malek.giffy.ui.recyclerview.BaseViewModelItem

class GIFItemViewModel(override val layout: Int = R.layout.gif_item_layout, val gif: GIF) :
    BaseViewModelItem() {
    val placeholder = ColorDrawable((Math.random() * 16777215).toInt() or (0xFF shl 24))
    fun onClick(view: View) {
        val destination =
            SearchFragmentDirections.actionNavigationDashboardToGIFDetailsFragment(
                gif.title,
                gif.imageUrl
            )
        view.findNavController().navigate(destination)
    }
}