package com.malek.giffy.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malek.giffy.domaine.GIFRepository
import com.malek.giffy.utilities.formatError
import com.malek.giffy.utilities.getGIFError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val gifRepository: GIFRepository,
    private val backgroundDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val _state: MutableStateFlow<HomeState> = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state
    private val _reload: MutableSharedFlow<Unit> = MutableSharedFlow()

    init {
        viewModelScope.launch(backgroundDispatcher) {
            _reload.map {
                _state.update {
                    it.copy(isLoading = true)
                }
                gifRepository.getRandomGif().map {
                    HomeState(
                        isLoading = false,
                        imageUrl = it.imageUrl,
                        errorString = null,
                        errorGIF = null
                    )
                }.recover {
                    it.toHomeState()
                }.getOrElse {
                    it.toHomeState()
                }
            }.collect { nextState ->
                _state.update {
                    nextState
                }
            }
        }
    }

    fun fetchNewGIF() {
        viewModelScope.launch {
            _reload.emit(Unit)
        }
    }


}


fun Throwable.toHomeState(): HomeState = HomeState(
    isLoading = false,
    imageUrl = null,
    errorString = this.formatError(),
    errorGIF = this.getGIFError()
)