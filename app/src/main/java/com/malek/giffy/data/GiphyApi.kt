package com.malek.giffy.data

import retrofit2.http.GET

interface GiphyApi {
    @GET("random?api_key=lDVuttP84V2ON7MpPoSZmghniWMHsdyt&tag=&rating=g")
    suspend fun getRandomGif(): GifJson
}