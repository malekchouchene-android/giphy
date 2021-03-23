package com.malek.giffy.ui.search

import androidx.lifecycle.viewModelScope
import com.malek.giffy.domaine.*
import com.malek.giffy.ui.BaseViewModel
import com.malek.giffy.utilities.formatError
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.random.Random

class SearchViewModel(private val repository: GIFRepository) :
    BaseViewModel<SearchState, SearchUserIntent>() {

    private var lastPagination: Pagination? = null
    private var lastQuery: String? = null
    private val images: MutableList<GIF> = mutableListOf()

    companion object {
        val randomEmptyTags = arrayOf("sorry", "not found", "oops", "sad")
    }

    override fun dispatchUserIntent(userIntent: SearchUserIntent) {
        when (userIntent) {
            is SearchUserIntent.NewQuery -> {
                viewModelScope.launch {
                    lastPagination = null
                    lastQuery = userIntent.query
                    lastPagination = null
                    _state.value = SearchState.Loading
                    repository.getGIFsByKeyword(keyWord = userIntent.query, offest = 0)
                        .map {
                            mapResultToState(result = it, newQuery = true)
                        }
                        .collect {
                            _state.value = it
                        }
                }
            }
            SearchUserIntent.NextPage -> {
                viewModelScope.launch {
                    lastQuery?.let { safeLastQuery ->
                        lastPagination?.let { safeLastPagination ->
                            if ((safeLastPagination.count + safeLastPagination.offset) < safeLastPagination.totalCount) {
                                _state.value = SearchState.RequestingNewPage
                                repository.getGIFsByKeyword(
                                    keyWord = safeLastQuery,
                                    offest = safeLastPagination.count + safeLastPagination.offset
                                ).map {
                                    mapResultToState(result = it, newQuery = false)
                                }.collect {
                                    _state.value = it
                                }
                            } else {
                                _state.value = SearchState.GetToEnd
                            }
                        }

                    }

                }
            }
        }
    }

    private fun getRandomEmptyStat() {
        viewModelScope.launch {
            repository.getRandomGif(randomEmptyTags[Random.nextInt(0, randomEmptyTags.size)])
                .collect { ramdomEmptyGif ->
                    if (ramdomEmptyGif is Result.Success) {
                        _state.value =
                            SearchState.EmptyStat(randomEmptyGIF = ramdomEmptyGif.data.preview)
                    }
                }
        }
    }

    private fun mapResultToState(result: Result<GIFList>, newQuery: Boolean): SearchState {
        return when (result) {
            is Result.Success -> {
                lastPagination = result.data.pagination
                if (result.data.images.isEmpty()) {
                    getRandomEmptyStat()
                    SearchState.EmptyStat(null)
                } else {
                    if (newQuery) {
                        images.clear()
                        images.addAll(result.data.images)
                    } else {
                        images.addAll(result.data.images)
                    }
                    SearchState.ImageListStat(images.map { GIFItemViewModel(gif = it) }.toList())

                }
            }
            is Result.Error -> {
                SearchState.ErrorStat(errorString = result.exception.formatError())
            }
        }
    }


}