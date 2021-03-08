package com.malek.giffy.ui.home

import androidx.lifecycle.viewModelScope
import com.malek.giffy.domaine.GIFRepository
import com.malek.giffy.domaine.Result
import com.malek.giffy.ui.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(private val gifRepository: GIFRepository) :
    BaseViewModel<HomeState, HomeUserIntent>() {


    private fun getNewImage() {
        _state.value= HomeState.Loading()
        viewModelScope.launch {
            when (val resultRandom = gifRepository.getRandomGif(null)) {
                is Result.Success -> {
                    _state.value = HomeState.ImageState(previewUrl = resultRandom.data.imageUrl)
                }
                is Result.Error -> {
                    _state.value = HomeState.ErrorStat(resultRandom.exception)
                }
            }

        }
    }

    override fun dispatchUserIntent(userIntent: HomeUserIntent) {
        getNewImage()
    }

}
