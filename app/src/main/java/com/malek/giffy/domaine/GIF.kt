package com.malek.giffy.domaine

import com.malek.giffy.data.GifDataJson

data class GIF(
    val imageUrl: String,
    val preview: String,
    val id: String,
    val title: String
)

fun GifDataJson.toDomaine(): GIF {
    return GIF(
        imageUrl = imagesJson.originalImageJson.url,
        preview = imagesJson.fixedHeightImageJson.url,
        id = id,
        title = name ?: ""
    )
}