package com.malek.giffy

import android.content.Context
import android.os.Build.VERSION_CODES.P
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.malek.giffy.domaine.Gif
import com.malek.giffy.domaine.GifRepository
import com.malek.giffy.domaine.Result
import com.malek.giffy.ui.home.HomeUserIntent
import com.malek.giffy.ui.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.HttpException
import java.lang.Exception
import java.net.ConnectException
import java.net.SocketTimeoutException

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [P])
@ExperimentalCoroutinesApi
class HomeViewModelTest {
    private val context: Context = ApplicationProvider.getApplicationContext<Context>()


    @JvmField
    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

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

    @After
    fun tearDown() {
        stopKoin()
    }

}

class GifRepositoryTest() : GifRepository {
    override suspend fun getRandomGif(): Result<Gif> {
        return Result.Success(data = Gif(imageUrl = "test", preview = "testPreview", id = "id"))
    }

    override suspend fun getGifsByKeyword(keyWord: String): Result<Gif> {
        TODO("Not yet implemented")
    }

}

class GifRepositoryError(val e: Exception) : GifRepository {
    override suspend fun getRandomGif(): Result<Gif> {
        return Result.Error(e)
    }

    override suspend fun getGifsByKeyword(keyWord: String): Result<Gif> {
        TODO("Not yet implemented")
    }
}

