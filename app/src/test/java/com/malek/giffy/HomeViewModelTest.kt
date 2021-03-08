package com.malek.giffy

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.malek.giffy.ui.home.HomeUserIntent
import com.malek.giffy.ui.home.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test
import java.lang.Exception
import java.net.ConnectException
import java.net.SocketTimeoutException


@ExperimentalCoroutinesApi
class HomeViewModelTest: BaseTestClass() {
    private val context: Context = ApplicationProvider.getApplicationContext<Context>()


    @Test
    fun should_not_be_loading_when_call_for_new_image() {
        //GIVEN
        val homeViewModel = HomeViewModel(GifRepositoryTest())
        //WHEN
        homeViewModel.dispatchUserIntent(HomeUserIntent.GetNewImage)
        //Then
        assertEquals(homeViewModel.state.value?.isLoading, false)
        assertEquals(homeViewModel.state.value?.imageUrl, "test")
        assertEquals(homeViewModel.state.value?.errorString, null)
    }

    @Test
    fun should_format_error_when_no_network() {
        //GIVEN
        val homeViewModel = HomeViewModel(GifRepositoryError(ConnectException()))
        //WHEN
        homeViewModel.dispatchUserIntent(HomeUserIntent.GetNewImage)
        //Then
        assertEquals(homeViewModel.state.value?.isLoading, false)
        assertEquals(homeViewModel.state.value?.imageUrl, null)
        assertEquals(
                context.getString(homeViewModel.state.value!!.errorString!!),
                context.getString(R.string.no_network_error)
        )
    }

    @Test
    fun should_format_error_when_time_out() {
        //GIVEN
        val homeViewModel = HomeViewModel(GifRepositoryError(SocketTimeoutException()))
        //WHEN
        homeViewModel.dispatchUserIntent(HomeUserIntent.GetNewImage)
        //Then
        assertEquals(homeViewModel.state.value?.isLoading, false)
        assertEquals(homeViewModel.state.value?.imageUrl, null)
        assertEquals(
                context.getString(homeViewModel.state.value!!.errorString!!),
                context.getString(R.string.no_network_error)
        )
    }

    @Test
    fun should_format_error_when_unexpected_error() {
        //GIVEN
        val homeViewModel = HomeViewModel(GifRepositoryError(Exception("Fake error")))
        //WHEN
        homeViewModel.dispatchUserIntent(HomeUserIntent.GetNewImage)
        //Then
        assertEquals(homeViewModel.state.value?.isLoading, false)
        assertEquals(homeViewModel.state.value?.imageUrl, null)
        assertEquals(
                context.getString(homeViewModel.state.value!!.errorString!!),
                context.getString(R.string.unexpected_error)
        )
    }


}

