package com.malek.giffy.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class State
abstract class UserIntent

abstract class BaseViewModel<T : State, U : UserIntent> : ViewModel() {

    abstract val state: MutableLiveData<T>

    abstract fun dispatchUserIntent(userIntent: U)

}