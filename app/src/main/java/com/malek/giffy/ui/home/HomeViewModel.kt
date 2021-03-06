package com.malek.giffy.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.malek.giffy.domaine.GifRepository
import com.malek.giffy.domaine.Result
import com.malek.giffy.ui.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(private val gifRepository: GifRepository) :
    BaseViewModel<HomeState, HomeUserIntent>() {

    override val state = MutableLiveData<HomeState>()

    private fun getNewImage() {
        state.value= HomeState.Loading()
        viewModelScope.launch {
            when (val resultRandom = gifRepository.getRandomGif()) {
                is Result.Success -> {
                    state.value = HomeState.ImageState(previewUrl = resultRandom.data.preview)
                }
                is Result.Error -> {
                    state.value = HomeState.ErrorStat(resultRandom.exception)
                }
            }

        }
    }

    override fun dispatchUserIntent(userIntent: HomeUserIntent) {
        getNewImage()
    }

}
