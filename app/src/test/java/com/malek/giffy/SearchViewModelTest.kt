package com.malek.giffy

import android.os.Build
import com.google.common.truth.Truth
import com.malek.giffy.domaine.*
import com.malek.giffy.ui.search.SearchState
import com.malek.giffy.ui.search.SearchUserIntent
import com.malek.giffy.ui.search.SearchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.net.ConnectException


@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
@ExperimentalCoroutinesApi
class SearchViewModelTest : BaseTestClass() {

    @Mock
    private lateinit var mockSuspendGetList: SuspendableMock<GIFList>

    @Test
    fun should_update_query_when_user_set_new_query() {
        val searchViewModel = SearchViewModel(repository = GifRepositoryTest())
        searchViewModel.dispatchUserIntent(SearchUserIntent.NewQuery("test"))
        Assert.assertEquals(searchViewModel.getPrivateProperty("lastQuery"), "test")
    }

    @Test
    fun should_update_pagination_when_user_set_new_query() {
        val searchViewModel = SearchViewModel(repository = GifRepositoryTest())
        searchViewModel.dispatchUserIntent(SearchUserIntent.NewQuery("test"))
        Assert.assertEquals(
            searchViewModel.getPrivateProperty("lastPagination"),
            Pagination(count = 1, totalCount = 1, offset = 0)
        )
    }

    @Test
    fun should_map_empty_state_when_no_result() {
        val searchViewModel = SearchViewModel(repository = GifRepositoryEmptyTest())
        searchViewModel.dispatchUserIntent(SearchUserIntent.NewQuery("empty"))
        Truth.assertThat(searchViewModel.state.value)
            .isInstanceOf(SearchState.EmptyStat::class.java)
    }

    @Test
    fun should_map_error_state_when_error() {
        val searchViewModel = SearchViewModel(repository = GifRepositoryError(ConnectException()))
        searchViewModel.dispatchUserIntent(SearchUserIntent.NewQuery("empty"))
        Truth.assertThat(searchViewModel.state.value)
            .isInstanceOf(SearchState.ErrorStat::class.java)
    }

    @Test
    fun should_not_call_repository_when_all_items_have_been_loaded() {
        //GIVEN
        BDDMockito.given(mockSuspendGetList.suspendFunctionMock()).willAnswer {
            Result.Success(
                GIFList(
                    images = arrayListOf(
                        GIF(
                            imageUrl = "test",
                            preview = "testPreview",
                            id = "id",
                            title = "title"
                        )
                    ), pagination = Pagination(count = 1, totalCount = 1, offset = 0)
                )
            )
        }
        val searchViewModel = SearchViewModel(repository = object : GIFRepository {
            override fun getRandomGif(tag: String?): Flow<Result<GIF>> {
                return flow {
                    emit(
                        Result.Success(
                            data = GIF(
                                imageUrl = "test",
                                preview = "testPreview",
                                id = "id",
                                title = "test"
                            )
                        )
                    )
                }

            }

            override suspend fun getGIFsByKeyword(
                keyWord: String,
                offest: Int
            ): Flow<Result<GIFList>> {
                return flow {
                    emit(mockSuspendGetList.suspendFunctionMock())
                }
            }

        })
        //WHEN

        searchViewModel.dispatchUserIntent(SearchUserIntent.NewQuery("test"))
        searchViewModel.dispatchUserIntent(SearchUserIntent.NextPage)
        //THEN
        Truth.assertThat(searchViewModel.state.value).isInstanceOf(SearchState.GetToEnd::class.java)
        verify(mockSuspendGetList, times(1)).suspendFunctionMock()

    }

    @Test
    fun should_call_with_right_offest_to_get_next_page() {
        //GIVEN
        val gifListMock = mutableListOf<GIF>()
        repeat(24) {
            gifListMock.add(
                GIF(
                    imageUrl = "test",
                    preview = "testPreview",
                    id = "id",
                    title = "title"
                )
            )
        }

        BDDMockito.given(mockSuspendGetList.suspendFunctionMock()).willAnswer {
            Result.Success(
                GIFList(
                    images = gifListMock.toList(),
                    pagination = Pagination(count = 24, totalCount = 1000, offset = 0)
                )
            )
        }
        var startRequest = true
        val searchViewModel = SearchViewModel(repository = object : GIFRepository {
            override fun getRandomGif(tag: String?): Flow<Result<GIF>> {
                return flow {
                    emit(
                        Result.Success(
                            data = GIF(
                                imageUrl = "test",
                                preview = "testPreview",
                                id = "id",
                                title = "tilte"
                            )
                        )
                    )
                }

            }

            override suspend fun getGIFsByKeyword(
                keyWord: String,
                offest: Int
            ): Flow<Result<GIFList>> {
                return flow {
                    //THEN
                    Truth.assertThat(keyWord).isEqualTo("test")

                    if (startRequest) {
                        Truth.assertThat(offest).isEqualTo(0)
                        startRequest = false
                    } else {
                        Truth.assertThat(offest).isEqualTo(24)

                    }
                    emit(mockSuspendGetList.suspendFunctionMock())
                }
            }

        })
        //WHEN

        searchViewModel.dispatchUserIntent(SearchUserIntent.NewQuery("test"))
        searchViewModel.dispatchUserIntent(SearchUserIntent.NextPage)

    }

}

