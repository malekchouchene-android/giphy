package com.malek.giffy

import com.malek.giffy.domaine.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class GifRepositoryTest() : GIFRepository {
    override fun getRandomGif(tag: String?): Flow<Result<GIF>> {
        return flow<Result<GIF>> {
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


    override suspend fun getGIFsByKeyword(keyWord: String, offest: Int): Flow<Result<GIFList>> {
        return flow {
            emit(
                Result.Success(
                    data = GIFList(
                        images = arrayListOf(
                            GIF(
                                imageUrl = "test",
                                preview = "testPreview",
                                id = "id",
                                title = "test"
                            )
                        ), pagination = Pagination(count = 1, totalCount = 1, offset = 0)
                    )
                )
            )
        }
    }

}

class GifRepositoryEmptyTest : GIFRepository {
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

    override suspend fun getGIFsByKeyword(keyWord: String, offest: Int): Flow<Result<GIFList>> {
        return flow {
            emit(
                Result.Success(
                    data = GIFList(
                        images = emptyList(),
                        pagination = Pagination(count = 0, totalCount = 0, offset = 0)
                    )
                )
            )
        }
    }

}


class GifRepositoryError(val e: Exception) : GIFRepository {
    override fun getRandomGif(tag: String?): Flow<Result<GIF>> {
        return flow {
            emit(Result.Error(e))
        }
    }

    override suspend fun getGIFsByKeyword(keyWord: String, offest: Int): Flow<Result<GIFList>> {
        return flow {
            emit(Result.Error(e))
        }
    }
}


interface SuspendableMock<T> {
    fun suspendFunctionMock(): Result<T>
}