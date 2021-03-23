package com.malek.giffy.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malek.giffy.domaine.GIFRepository
import com.malek.giffy.domaine.Result
import com.malek.giffy.ui.BaseViewModel
import com.malek.giffy.utilities.formatError
import com.malek.giffy.utilities.getGIFError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(private val gifRepository: GIFRepository) :
    BaseViewModel<HomeState, HomeUserIntent>() {

    private fun getNewImage() {
        _state.value = HomeState.Loading
        viewModelScope.launch {
            gifRepository.getRandomGif(null)
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _state.value = HomeState.ImageState(it.data.imageUrl)
                        }
                        is Result.Error -> {
                            _state.value = HomeState.ErrorStat(it.exception)
                        }
                    }
                }

        }
    }

    override fun dispatchUserIntent(userIntent: HomeUserIntent) {
        getNewImage()
    }

}
