package com.malek.giffy.data

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyApi {
    @GET("random?api_key=lDVuttP84V2ON7MpPoSZmghniWMHsdyt&tag=&rating=g")
    suspend fun getRandomGif(@Query("tag") tag: String?): GifResultJson

    @GET("search?api_key=lDVuttP84V2ON7MpPoSZmghniWMHsdyt")
    suspend fun getGifListByKeyWord(
        @Query("q") keyword: String,
        @Query("limit") limit: Int = 24,
        @Query("offset") offest: Int = 0
    ): GifListJson

}