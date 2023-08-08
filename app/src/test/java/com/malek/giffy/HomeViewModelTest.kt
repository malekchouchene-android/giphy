package com.malek.giffy

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth
import com.malek.giffy.domaine.GIF
import com.malek.giffy.domaine.GIFRepository
import com.malek.giffy.ui.home.HomeViewModel
import com.malek.giffy.utilities.formatError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.net.ConnectException


@ExperimentalCoroutinesApi
class HomeViewModelTest : BaseTestClass() {
    private val context: Context = ApplicationProvider.getApplicationContext<Context>()
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    @Mock
    lateinit var gifRepository: GIFRepository

    @Test
    fun `test fetchNewGIF calls getRandomGif and updates the state`() = runTest(testDispatcher) {
        // Given
        val expectedImageUrl = "https://example.com/image.gif"
        Mockito.`when`(gifRepository.getRandomGif()).thenReturn(
            Result.success(
                GIF(
                    imageUrl = "https://example.com/image.gif",
                    preview = "preview",
                    id = "1",
                    title = "test"
                )
            )
        )
        val homeViewModel = HomeViewModel(gifRepository, testDispatcher)


        // When
        homeViewModel.fetchNewGIF()

        // Then
        val currentState = homeViewModel.state.value
        assertEquals(true, currentState.isLoading) // Expect isLoading to be true during the fetch
        advanceUntilIdle() // Advance the test until all coroutines are completed
        val updatedState = homeViewModel.state.value
        assertEquals(false, updatedState.isLoading) // Expect isLoading to be false after fetch
        assertEquals(expectedImageUrl, updatedState.imageUrl)
        assertEquals(null, updatedState.errorString)
        assertEquals(null, updatedState.errorGIF)
    }

    @Test
    fun `test fetchNewGIF handles repository error and updates the state`() =
        runTest(testDispatcher) {
            // Given
            val expectedError = Exception("GIF fetch error")
            `when`(gifRepository.getRandomGif()).thenReturn(Result.failure(expectedError))
            val homeViewModel = HomeViewModel(gifRepository, testDispatcher)


            // When
            homeViewModel.fetchNewGIF()
            // When

            // Then
            val currentState = homeViewModel.state.value
            assertEquals(true, currentState.isLoading)
            advanceUntilIdle()
            val updatedState = homeViewModel.state.value
            assertEquals(false, updatedState.isLoading)
            assertEquals(null, updatedState.imageUrl)
            assertEquals(expectedError.formatError(), updatedState.errorString)
            Truth.assertThat(updatedState.errorGIF).isNotNull()
        }

    @Test
    fun should_format_error_when_no_network() {
        runTest {
            //GIVEN
            Mockito.`when`(gifRepository.getRandomGif())
                .thenReturn(Result.failure(ConnectException()))
            val homeViewModel = HomeViewModel(gifRepository, testDispatcher)
            //WHEN
            homeViewModel.fetchNewGIF()
            advanceUntilIdle()

            //Then
            assertEquals(homeViewModel.state.value.isLoading, false)
            assertEquals(homeViewModel.state.value.imageUrl, null)
            assertEquals(
                context.getString(homeViewModel.state.value.errorString!!),
                context.getString(R.string.no_network_error)
            )
        }

    }


}

