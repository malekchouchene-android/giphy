package com.malek.giffy.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malek.giffy.domaine.GIF
import com.malek.giffy.domaine.GIFList
import com.malek.giffy.domaine.GIFRepository
import com.malek.giffy.domaine.Pagination
import com.malek.giffy.utilities.formatError
import com.malek.giffy.utilities.getGIFError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class SearchViewModel(
    private val repository: GIFRepository,
    val backgroundDispatcher: CoroutineDispatcher = Dispatchers.IO
) :
    ViewModel() {
    private val _state: MutableStateFlow<SearchState> = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state
    private var lastPagination: Pagination? = null
    private var lastQuery: String? = null

    companion object {
        val randomEmptyTags = arrayOf("sorry", "not found", "oops", "sad")
    }

    private val _searchQueryGIF: MutableSharedFlow<SearchQueryGIF> =
        MutableSharedFlow()

    init {
        viewModelScope.launch(backgroundDispatcher) {
            _searchQueryGIF.collectLatest {
                val result = repository.getGIFsByKeyword(keyWord = it.query, offest = it.offset)
                mapResultToState(result)

            }
        }

    }

    fun dispatchUserAction(userIntent: UserAction) {
        when (userIntent) {
            is UserAction.NewQuery -> {
                viewModelScope.launch(backgroundDispatcher) {
                    lastPagination = null
                    lastQuery = userIntent.query
                    _state.update {
                        SearchState(imageList = null, null, errorGIF = null, loading = true, false)
                    }
                    _searchQueryGIF.emit(
                        SearchQueryGIF(query = userIntent.query, offset = 0)
                    )
                }
            }

            UserAction.NextPage -> {
                viewModelScope.launch {
                    lastQuery?.let { safeLastQuery ->
                        lastPagination?.let { safeLastPagination ->
                            if (
                                (safeLastPagination.count + safeLastPagination.offset) < safeLastPagination.totalCount
                            ) {
                                _state.update {
                                    it.copy(loading = true)
                                }
                                _searchQueryGIF.emit(
                                    SearchQueryGIF(
                                        query = safeLastQuery,
                                        offset = safeLastPagination.count + safeLastPagination.offset
                                    )
                                )

                            } else {
                                _state.update {
                                    it.copy(allImageLoaded = true)
                                }
                            }
                        }

                    }

                }
            }
        }
    }

    private fun setRandomEmptyStat() {
        viewModelScope.launch(backgroundDispatcher) {
            repository.getRandomGif(randomEmptyTags[Random.nextInt(0, randomEmptyTags.size)])
                .onSuccess { gif ->
                    _state.update {
                        SearchState(
                            emptyList(),
                            errorString = null,
                            errorGIF = gif.preview,
                            loading = true,
                            allImageLoaded = true
                        )
                    }
                }.onFailure {
                    _state.update { _ ->
                        SearchState(
                            emptyList(),
                            errorString = null,
                            errorGIF = it.getGIFError(),
                            loading = true,
                            allImageLoaded = true
                        )
                    }
                }
        }
    }

    private fun mapResultToState(result: Result<GIFList>) {
        result.onSuccess {
            lastPagination = it.pagination
            if (it.images.isEmpty()) {
                setRandomEmptyStat()
            } else {
                _state.update { lastState ->
                    buildImageListState(it, lastState.imageList)
                }
            }
        }.onFailure { t ->
            _state.update {
                it.copy(
                    loading = false,
                    errorString = t.formatError(),
                    errorGIF = t.getGIFError(),
                    allImageLoaded = false
                )
            }
        }
    }

    private fun buildImageListState(
        gifList: GIFList,
        lastImages: List<GIF>?
    ): SearchState {
        val newListImages = lastImages.orEmpty().toMutableList()
        newListImages.addAll(gifList.images)
        return if (gifList.pagination.count.toLong() == gifList.pagination.totalCount) {
            SearchState(
                imageList = newListImages,
                allImageLoaded = true,
                loading = false,
                errorString = null,
                errorGIF = null
            )
        } else {
            SearchState(
                imageList = newListImages,
                loading = false,
                allImageLoaded = false,
                errorString = null,
                errorGIF = null
            )
        }
    }

}

data class SearchQueryGIF(val query: String, val offset: Int)