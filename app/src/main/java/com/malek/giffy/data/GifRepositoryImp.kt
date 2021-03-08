package com.malek.giffy.data

import com.malek.giffy.domaine.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class GifRepositoryImp(private val giphyApi: GiphyApi) : GIFRepository {
    override suspend fun getRandomGif(tag: String?): Result<GIF> {
        return withContext(Dispatchers.IO) {
            try {
                val gifDataJson = giphyApi.getRandomGif(tag).gifDataJson
                Result.Success<GIF>(
                    gifDataJson.toDomaine()
                )
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    override suspend fun getGIFsByKeyword(keyWord: String, offest: Int): Result<GIFList> {
        return withContext(Dispatchers.IO) {
            try {
                val gifListJson = giphyApi.getGifListByKeyWord(keyWord, offest = offest)
                Result.Success(
                    GIFList(
                        images = gifListJson.gifDataJsons.map {
                            it.toDomaine()
                        },
                        pagination = gifListJson.paginationJson.toDomaine()
                    )
                )

            } catch (e: Exception) {
                Result.Error(e)

            }
        }
    }
}
