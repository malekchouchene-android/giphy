package com.malek.giffy.ui.home

import com.malek.giffy.ui.State
import com.malek.giffy.ui.formatError

sealed class HomeState(val isLoading: Boolean, val imageUrl: String?, val errorString: Int?):State() {
    class Loading : HomeState(isLoading = true, imageUrl = null, errorString = null)
    class ImageState(previewUrl: String) : HomeState(isLoading = false, imageUrl = previewUrl, errorString = null)
    class ErrorStat(e:Exception) : HomeState(isLoading = false, imageUrl = null, errorString = e.formatError())
}

