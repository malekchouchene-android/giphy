package com.malek.giffy.data

import com.malek.giffy.domaine.*
import com.malek.giffy.utilities.runSuspendCatching
import kotlinx.coroutines.CloseableCoroutineDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.lang.Exception

class GifRepositoryImp(
    private val giphyApi: GiphyApi,
    private val backgroundDispatcher: CoroutineDispatcher = Dispatchers.IO
) : GIFRepository {
    override suspend fun getRandomGif(tag: String?): Result<GIF> {
        return  withContext(backgroundDispatcher){
            runSuspendCatching {
                giphyApi.getRandomGif(tag).gifDataJson.toDomaine()
            }
        }

    }


    override suspend fun getGIFsByKeyword(keyWord: String, offest: Int): Result<GIFList> {
        return withContext(backgroundDispatcher){
            runSuspendCatching {
                val gifListJson = giphyApi.getGifListByKeyWord(keyWord, offest)
                GIFList(
                    images = gifListJson.gifDataJsons.map { it.toDomaine() }, pagination =
                    gifListJson.paginationJson.toDomaine()
                )
            }
        }
    }
}
