package com.malek.giffy

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.example.compose.AppTheme
import com.malek.giffy.ui.details.GIFDetailsActivity
import com.malek.giffy.ui.home.HomeScreen
import com.malek.giffy.ui.home.HomeViewModel
import com.malek.giffy.ui.search.SearchScreen
import com.malek.giffy.ui.search.SearchViewModel
import com.malek.giffy.ui.search.UserAction
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val homeViewModel by viewModel<HomeViewModel>()
    private val searchViewViewModel by viewModel<SearchViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                var selected by remember {
                    mutableStateOf(Screen.Home)
                }
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            Screen.values().forEach { screen ->
                                NavigationBarItem(
                                    icon = {
                                        Icon(
                                            imageVector = screen.icon,
                                            contentDescription = stringResource(screen.title),
                                        )
                                    },
                                    label = {
                                        Text(
                                            stringResource(screen.title)
                                        )
                                    },
                                    selected = selected == screen,
                                    onClick = {
                                        selected = screen
                                    }
                                )
                            }
                        }
                    },
                ) { innerPadding ->
                    when (selected) {
                        Screen.Home -> {
                            HomeScreen(
                                homeStateFlow = homeViewModel.state,
                                onFetchNewGifRequest = {
                                    homeViewModel.fetchNewGIF()
                                }, modifier = Modifier.padding(innerPadding)
                            )
                        }

                        Screen.Search -> {
                            SearchScreen(
                                searchScreenState = searchViewViewModel.state,
                                onUserAction = { searchViewViewModel.dispatchUserAction(it) },
                                modifier = Modifier.padding(innerPadding),
                                onItemClick = {
                                    startActivity(
                                        Intent(
                                            this,
                                            GIFDetailsActivity::class.java
                                        ).apply {
                                            this.putExtra(
                                                GIFDetailsActivity.GIF_URL_KEY,
                                                it.imageUrl
                                            ).putExtra(
                                                GIFDetailsActivity.GIF_TITLE_KEY,
                                                it.title
                                            )
                                        })
                                }
                            )
                        }
                    }
                }
            }

        }
    }
}


enum class Screen(@StringRes val title: Int, val icon: ImageVector) {
    Home(R.string.title_home, icon = Icons.Filled.Home),
    Search(R.string.title_dashboard, icon = Icons.Filled.Search)
}

