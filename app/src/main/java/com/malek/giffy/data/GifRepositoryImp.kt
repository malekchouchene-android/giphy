package com.malek.giffy.data

import com.malek.giffy.domaine.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.lang.Exception

class GifRepositoryImp(private val giphyApi: GiphyApi) : GIFRepository {
    override fun getRandomGif(tag: String?): Flow<Result<GIF>> {
        return flow {
            try {
                emit(Result.Success(giphyApi.getRandomGif(tag).gifDataJson.toDomaine()))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
    }


    override suspend fun getGIFsByKeyword(keyWord: String, offest: Int): Flow<Result<GIFList>> {
        return flow {
            try {
                val gifListJson = giphyApi.getGifListByKeyWord(keyWord, offest = offest)
                emit(
                    Result.Success(
                        GIFList(
                            images = gifListJson.gifDataJsons.map {
                                it.toDomaine()
                            },
                            pagination = gifListJson.paginationJson.toDomaine()
                        )
                    )
                )
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }.flowOn(Dispatchers.IO)


    }
}
