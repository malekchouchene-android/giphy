package com.malek.giffy.ui.home

import com.malek.giffy.utilities.formatError
import com.malek.giffy.utilities.getGIFError

sealed class HomeState(
    val isLoading: Boolean,
    open val imageUrl: String?,
    open val errorString: Int?,
    open val errorGIF: Int? = null
) {
    object Init : HomeState(isLoading = true, imageUrl = null, errorString = null)
    class ImageState(override val imageUrl: String) :
        HomeState(isLoading = false, imageUrl = imageUrl, errorString = null)

    class ErrorStat(override val errorString: Int, override val errorGIF: Int) : HomeState(
        isLoading = false,
        imageUrl = null,
        errorString = errorString,
        errorGIF = errorGIF
    )

    class Loading(previewUrl: String?) :
        HomeState(isLoading = true, imageUrl = previewUrl, errorString = null, errorGIF = null)
}

fun Throwable.toErrorStat() =
    HomeState.ErrorStat(errorGIF = this.getGIFError(), errorString = this.formatError())
