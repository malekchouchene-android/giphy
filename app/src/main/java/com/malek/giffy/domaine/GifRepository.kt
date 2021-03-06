package com.malek.giffy.domaine

interface GifRepository {
    suspend fun getRandomGif(): Result<Gif>
    suspend fun getGifsByKeyword(keyWord: String): Result<Gif>
}