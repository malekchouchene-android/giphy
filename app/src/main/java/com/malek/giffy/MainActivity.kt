package com.malek.giffy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.malek.giffy.ui.home.HomeScreen
import com.malek.giffy.ui.home.HomeViewModel
import com.malek.giffy.ui.search.SearchScreen
import com.malek.giffy.ui.search.SearchViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val mainViewModel by viewModels<MainViewModel>()
    private val homeViewModel by viewModel<HomeViewModel>()
    private val searchViewViewModel by viewModel<SearchViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                var selected by remember {
                    mutableStateOf<Screen>(Screen.Home)
                }
                Scaffold(
                    bottomBar = {
                        BottomNavigation {
                            Screen.values().forEach { screen ->
                                BottomNavigationItem(
                                    icon = {
                                        Icon(
                                            imageVector = screen.icon,
                                            contentDescription = stringResource(screen.title)
                                        )
                                    },
                                    label = { Text(stringResource(screen.title)) },
                                    selected = selected == screen,
                                    onClick = {
                                        selected = screen
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    when (selected) {
                        Screen.Home -> {
                            HomeScreen(homeStateFlow = homeViewModel.state, onFetchNewGifRequest = {
                                homeViewModel.fetchNewGIF()
                            }, modifier = Modifier.padding(innerPadding))
                        }

                        Screen.Search -> {
                            SearchScreen(
                                searchScreenState = searchViewViewModel.state,
                                onUserAction = { searchViewViewModel.dispatchUserAction(it) })
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.fetchNewGIF()
    }
}


enum class Screen(@StringRes val title: Int, val icon: ImageVector) {
    Home(R.string.title_home, icon = Icons.Filled.Home),
    Search(R.string.title_dashboard, icon = Icons.Filled.Search)
}

