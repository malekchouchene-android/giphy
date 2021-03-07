package com.malek.giffy.data

import com.malek.giffy.domaine.Gif
import com.malek.giffy.domaine.GifRepository
import com.malek.giffy.domaine.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class GifRepositoryImp(private val giphyApi: GiphyApi) : GifRepository {
    override suspend fun getRandomGif(): Result<Gif> {
        return withContext(Dispatchers.IO) {
            try {
                val gifDataJson = giphyApi.getRandomGif().gifDataJson
                Result.Success<Gif>(
                    Gif(
                        imageUrl = gifDataJson.imagesJson.originalImageJson.url,
                        preview = gifDataJson.imagesJson.fixedHeightImageJson.url,
                        id = gifDataJson.id
                    )
                )
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    override suspend fun getGifsByKeyword(keyWord: String): Result<Gif> {
        TODO("Not yet implemented")
    }
}