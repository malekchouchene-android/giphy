package com.malek.giffy

import com.google.common.truth.Truth
import com.malek.giffy.domaine.GIF
import com.malek.giffy.domaine.GIFList
import com.malek.giffy.domaine.GIFRepository
import com.malek.giffy.domaine.Pagination
import com.malek.giffy.ui.search.SearchViewModel
import com.malek.giffy.ui.search.UserAction
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class SearchViewModelTest {

    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    @Mock
    lateinit var gifRepository: GIFRepository

    @Test
    fun should_update_pagination_when_user_set_new_query() = runTest(testDispatcher) {
        //given
        `when`(gifRepository.getGIFsByKeyword("test", 0)).thenReturn(
            Result.success(
                GIFList(
                    images = listOf<GIF>(
                        GIF(
                            imageUrl = "https://example.com/image.gif",
                            preview = "preview",
                            id = "1",
                            title = "test"
                        )
                    ),
                    pagination = Pagination(count = 1, totalCount = 1, offset = 0)
                )
            )
        )
        //when
        val searchViewModel = SearchViewModel(repository = gifRepository, testDispatcher)
        advanceUntilIdle()

        searchViewModel.dispatchUserAction(UserAction.NewQuery("test"))
        // then
        val initState = searchViewModel.state.value
        advanceUntilIdle()

        println(initState.toString())
        advanceUntilIdle()
        val currentState = searchViewModel.state.value
        println(initState.toString())

        Truth.assertThat(currentState.loading).isFalse()
        Truth.assertThat(currentState.imageList).isEqualTo(
            listOf<GIF>(
                GIF(
                    imageUrl = "https://example.com/image.gif",
                    preview = "preview",
                    id = "1",
                    title = "test"
                )
            )
        )
        Truth.assertThat(currentState.errorGIF).isNull()
        Truth.assertThat(currentState.allImageLoaded).isTrue()
    }
}