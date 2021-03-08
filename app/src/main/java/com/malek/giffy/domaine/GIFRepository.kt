package com.malek.giffy.domaine

interface GIFRepository {
    suspend fun getRandomGif(tag: String?): Result<GIF>
    suspend fun getGIFsByKeyword(keyWord: String, offest: Int): Result<GIFList>
}