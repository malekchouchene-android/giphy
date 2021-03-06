package com.malek.giffy.ui.home

import android.util.Log
import androidx.lifecycle.*
import com.malek.giffy.domaine.GifRepository
import com.malek.giffy.domaine.Result
import kotlinx.coroutines.launch

class HomeViewModel(private val gifRepository: GifRepository) : ViewModel() {

    val imagePreview = MutableLiveData<String>()

    init {
        viewModelScope.launch {
            when (val resultRandom = gifRepository.getRandomGif()) {
                is Result.Success -> {
                    Log.e("HomeViewModel", resultRandom.data.id)
                    imagePreview.value=resultRandom.data.preview
                }
                is Result.Error -> {
                    Log.e("HomeViewModelError", resultRandom.exception.toString())

                }
            }

        }

    }

}
