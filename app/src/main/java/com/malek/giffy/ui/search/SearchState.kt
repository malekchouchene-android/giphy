package com.malek.giffy.ui.search

import com.malek.giffy.domaine.GIF

data class SearchState(
    val imageList: List<GIF>? = null,
    val errorString: Int? = null,
    val errorGIF: Any? = null,
    val loading: Boolean = false,
    val allImageLoaded: Boolean = false
)
