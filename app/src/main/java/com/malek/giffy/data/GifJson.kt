package com.malek.giffy.data

import com.google.gson.annotations.SerializedName

data class GifDataJson(
    @SerializedName("id") val id: String,
    @SerializedName("image_original_url") val imageOriginalUrl: String,
    @SerializedName("fixed_height_small_url") val imagePreviewUrl: String
)

data class GifJson(
    @SerializedName("data") val gifDataJson: GifDataJson
)

