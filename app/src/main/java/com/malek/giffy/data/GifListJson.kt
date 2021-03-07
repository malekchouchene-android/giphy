package com.malek.giffy.data

import com.google.gson.annotations.SerializedName

data class GifListJson(@SerializedName("data") val gifDataJsons: List<GifDataJson>,@SerializedName("pagination")val paginationJson: PaginationJson)

data class PaginationJson(
    @SerializedName("total_count") val totalCount: Long,
    @SerializedName("count") val count: Int,
    @SerializedName("offset") val offset: Int
)
