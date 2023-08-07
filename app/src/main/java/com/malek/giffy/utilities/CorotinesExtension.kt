package com.malek.giffy.utilities

import kotlinx.coroutines.CancellationException


inline fun <T> runSuspendCatching(f: () -> T): Result<T> {
    return try {
        Result.success(f())
    } catch (e: Throwable) {
        if(e is CancellationException) throw  e
        Result.failure(e)
    }
}