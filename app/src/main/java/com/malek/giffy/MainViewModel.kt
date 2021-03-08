package com.malek.giffy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _hideNavView: MutableLiveData<Boolean> =
        MutableLiveData(false)
    val hideNavView: LiveData<Boolean> = _hideNavView

    fun shouldHideNavView() {
        _hideNavView.value = true
    }

    fun shouldShowNavView() {
        _hideNavView.value = false
    }
}