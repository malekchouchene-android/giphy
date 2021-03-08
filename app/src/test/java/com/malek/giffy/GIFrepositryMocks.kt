package com.malek.giffy

import com.malek.giffy.domaine.*
import java.lang.Exception

class GifRepositoryTest() : GIFRepository {
    override suspend fun getRandomGif(tag: String?): Result<GIF> {
        return Result.Success(data = GIF(imageUrl = "test", preview = "testPreview", id = "id", previewWidth = 200, previewHeight = 200))
    }

    override suspend fun getGIFsByKeyword(keyWord: String, offest: Int): Result<GIFList> {
        return Result.Success(data = GIFList(images = arrayListOf(GIF(imageUrl = "test", preview = "testPreview", id = "id", previewWidth = 200, previewHeight = 200)), pagination = Pagination(count = 1, totalCount = 1, offset = 0)))
    }

}

class GifRepositoryEmptyTest : GIFRepository {
    override suspend fun getRandomGif(tag: String?): Result<GIF> {
        return Result.Success(data = GIF(imageUrl = "test", preview = "testPreview", id = "id", previewWidth = 200, previewHeight = 200))
    }

    override suspend fun getGIFsByKeyword(keyWord: String, offest: Int): Result<GIFList> {
        return Result.Success(data = GIFList(images = emptyList(), pagination = Pagination(count = 0, totalCount = 0, offset = 0)))
    }

}


class GifRepositoryError(val e: Exception) : GIFRepository {
    override suspend fun getRandomGif(tag: String?): Result<GIF> {
        return Result.Error(e)
    }

    override suspend fun getGIFsByKeyword(keyWord: String, offest: Int): Result<GIFList> {
        return Result.Error(e)
    }
}


