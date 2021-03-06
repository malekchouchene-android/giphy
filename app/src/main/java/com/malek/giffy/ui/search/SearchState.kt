package com.malek.giffy.ui.search

import com.malek.giffy.domaine.GIF
import com.malek.giffy.ui.State

sealed class SearchState(
    val imageList: List<GIFItemViewModel>?,
    val errorString: Int?,
    val randomEmptyGIF: String? = null
) : State() {
    object Loading : SearchState(imageList = null, errorString = null)
    object RequestingNewPage : SearchState(imageList = null, errorString = null)
    class ImageListStat(imageList: List<GIFItemViewModel>) :
        SearchState(imageList = imageList, errorString = null)

    class ErrorStat(errorString: Int) : SearchState(imageList = null, errorString = errorString)
    class EmptyStat(randomEmptyGIF: String?) :
        SearchState(imageList = emptyList(), errorString = null, randomEmptyGIF = randomEmptyGIF)

    object GetToEnd : SearchState(imageList = null, errorString = null)
}
