package com.malek.giffy.domaine

import com.malek.giffy.data.PaginationJson


data class Pagination(
    val totalCount: Long,
    val count: Int,
    val offset: Int
)

fun PaginationJson.toDomaine(): Pagination {
    return Pagination(totalCount = totalCount, count = count, offset = offset)
}