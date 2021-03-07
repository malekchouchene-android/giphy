package com.malek.giffy.data

import com.google.gson.annotations.SerializedName

data class GifDataJson(
    @SerializedName("id") val id: String,
    @SerializedName("images") val imagesJson: ImagesJson
)

data class GifResultJson(
    @SerializedName("data") val gifDataJson: GifDataJson
)

data class ImagesJson(
    @SerializedName("original") val originalImageJson: ImageItemJson,
    @SerializedName("fixed_height") val fixedHeightImageJson: ImageItemJson
)

data class ImageItemJson(
    @SerializedName("height") val height: String,
    @SerializedName("width") val width: String,
    @SerializedName("url") val url: String
)

