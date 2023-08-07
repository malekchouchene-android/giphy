package com.malek.giffy.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.malek.giffy.R
import com.malek.giffy.domaine.GIF
import com.malek.giffy.ui.home.GIFError
import com.malek.giffy.ui.home.GIFPreview
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchScreenState: StateFlow<SearchState>,
    onUserAction: (UserAction) -> Unit
) {
    MaterialTheme {
        val state = searchScreenState.collectAsState()
        val stateValue = state.value
        Scaffold(topBar = {
            SearchView(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
                onQuerySubmit = {
                    onUserAction(UserAction.NewQuery(it))
                })
        }, modifier = modifier.fillMaxSize()) { safePadding ->
            stateValue.imageList?.let { gifList ->
                if (gifList.isEmpty()) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(safePadding),
                        contentAlignment = Alignment.Center
                    ) {
                        when (val errorGif = stateValue.errorGIF) {
                            is String -> {
                                GIFPreview(previewUrl = errorGif)
                            }

                            is Int -> {
                                GIFError(errorGif = errorGif)
                            }
                        }

                    }
                } else {
                    GIFGrid(
                        modifier = Modifier
                            .fillMaxSize(),
                        items = gifList,
                        contentPadding = PaddingValues(
                            top = safePadding.calculateTopPadding() + 16.dp,
                            bottom = safePadding.calculateBottomPadding() + 16.dp,
                            start = safePadding.calculateStartPadding(LayoutDirection.Rtl) + 16.dp,
                            end = safePadding.calculateEndPadding(LayoutDirection.Rtl) + 16.dp
                        ),
                        onItemClick = {

                        },
                        onNextPageRequested = {
                            UserAction.NextPage
                        },
                        allItemLoaded = stateValue.allImageLoaded
                    )


                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(modifier: Modifier, onQuerySubmit: (String) -> Unit) {
    var query by remember { mutableStateOf("") }
    OutlinedTextField(
        value = query,
        trailingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "Recherche",
                modifier = Modifier.clickable {
                    if (query.length > 2)
                        onQuerySubmit(query)
                })
        },
        onValueChange = {
            query = it
        },
        placeholder = { Text(text = stringResource(id = R.string.search_hint)) },
        modifier = modifier,
        keyboardActions = KeyboardActions {
            onQuerySubmit(query)
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
    )
}

@Composable
fun GIFGrid(
    modifier: Modifier = Modifier,
    items: List<GIF>,
    onItemClick: (GIF) -> Unit,
    onNextPageRequested: () -> Unit,
    allItemLoaded: Boolean = false,
    contentPadding: PaddingValues
) {
    val lazyHorizontalGridState = rememberLazyGridState()
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem =
                lazyHorizontalGridState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.let {
                it.index == lazyHorizontalGridState.layoutInfo.totalItemsCount - 1
            } ?: false
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items, key = {
            it.id
        }) {
            GIFPreview(
                previewUrl = it.preview,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .clickable {
                        onItemClick(it)
                    })
        }
    }

    LaunchedEffect(key1 = shouldLoadMore) {
        snapshotFlow {
            shouldLoadMore.value
        }.filter {
            it && !allItemLoaded
        }.collectLatest {
            onNextPageRequested()
        }
    }

}


