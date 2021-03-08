package com.malek.giffy.domaine

import com.malek.giffy.data.GifDataJson

data class GIF(val imageUrl: String, val preview: String, val id: String, val previewWidth: Int, val previewHeight: Int)

fun GifDataJson.toDomaine(): GIF {
    return GIF(
            imageUrl = imagesJson.originalImageJson.url,
            preview = imagesJson.fixedHeightImageJson.url,
            id = id,
            previewHeight = imagesJson.fixedHeightImageJson.height.toInt(),
            previewWidth = imagesJson.fixedHeightImageJson.width.toInt()
    )
}