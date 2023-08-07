package com.malek.giffy.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malek.giffy.domaine.GIFRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val gifRepository: GIFRepository,
    private val backgroundDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val _state: MutableStateFlow<HomeState> = MutableStateFlow(HomeState.Init)
    val state: StateFlow<HomeState> = _state
    private val _reload: MutableSharedFlow<Unit> = MutableSharedFlow()

    init {
        viewModelScope.launch {
            _reload.onEach {
                _state.update {
                    HomeState.Loading(it.imageUrl)
                }
                withContext(backgroundDispatcher) {
                    _state.update {
                        gifRepository.getRandomGif().map {
                            HomeState.ImageState(it.imageUrl)
                        }.recover {
                            it.toErrorStat()
                        }.getOrElse {
                            it.toErrorStat()
                        }
                    }
                }
            }.collect()
        }
    }

    fun fetchNewGIF() {
        viewModelScope.launch {
            _reload.emit(Unit)
        }
    }


}
