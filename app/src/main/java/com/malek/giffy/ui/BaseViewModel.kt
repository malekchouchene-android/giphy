package com.malek.giffy.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class State
abstract class UserIntent

abstract class BaseViewModel<T : State, U : UserIntent>() :
    ViewModel() {
    protected val _state = MutableLiveData<T>()
    val state: LiveData<T> = _state

    abstract fun dispatchUserIntent(userIntent: U)

}