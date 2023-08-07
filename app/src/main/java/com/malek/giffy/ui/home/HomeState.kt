package com.malek.giffy.ui.home

data class HomeState(
    val isLoading: Boolean = true,
    val imageUrl: String? = null,
    val errorString: Int? = null,
    val errorGIF: Int? = null
)