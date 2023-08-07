package com.malek.giffy.ui.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.malek.giffy.R
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeStateFlow: StateFlow<HomeState>,
    onFetchNewGifRequest: () -> Unit
) {
    val resources = LocalContext.current.resources

    MaterialTheme {
        val state = homeStateFlow.collectAsState()
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        val errorString = remember {
            derivedStateOf {
                state.value.errorString
            }
        }
        val snackBarMessage = errorString.value
        if (snackBarMessage != null) {
            LaunchedEffect(key1 = "snackBar") {
                scope.launch {
                    val result = snackbarHostState.showSnackbar(
                        message = resources.getString(
                            snackBarMessage
                        ),
                        actionLabel = resources.getString(R.string.retry)
                    )
                    when (result) {
                        SnackbarResult.Dismissed -> {

                        }

                        SnackbarResult.ActionPerformed -> {
                            onFetchNewGifRequest()
                        }
                    }
                }
            }
        }
        val refreshing by remember {
            derivedStateOf {
                state.value.isLoading
            }
        }
        val pullRefreshState =
            rememberPullRefreshState(refreshing = refreshing, onRefresh = {
                onFetchNewGifRequest()

            })

        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState, true),
            snackbarHost = { SnackbarHost(snackbarHostState) }) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(it)
                    .pullRefresh(pullRefreshState),
                contentAlignment = Alignment.Center
            ) {

                when (val value = state.value) {
                    is HomeState.ErrorStat -> {
                        GIFError(errorGif = value.errorGIF)
                    }

                    is HomeState.ImageState -> {
                        GIFPreview(
                            modifier = Modifier.fillMaxWidth(),
                            previewUrl = value.imageUrl
                        )
                    }

                    HomeState.Init, is HomeState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .width(200.dp)
                        )
                    }
                }
            }
        }

    }
}


@Composable
fun GIFPreview(modifier: Modifier = Modifier, previewUrl: String, placeholder: Painter? = null) {
    AsyncImage(
        model = previewUrl,
        contentDescription = "",
        modifier = modifier,
        placeholder = placeholder,
        contentScale = ContentScale.FillWidth
    )
}


@Composable
fun GIFError(modifier: Modifier = Modifier, @DrawableRes errorGif: Int) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current).data(errorGif).build(),
        contentDescription = "Error",
        modifier = modifier
            .width(200.dp)
    )
}
