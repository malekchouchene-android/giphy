package com.malek.giffy.domaine

import kotlinx.coroutines.flow.Flow

interface GIFRepository {
    fun getRandomGif(tag: String?): Flow<Result<GIF>>
    suspend fun getGIFsByKeyword(keyWord: String, offest: Int): Flow<Result<GIFList>>
}