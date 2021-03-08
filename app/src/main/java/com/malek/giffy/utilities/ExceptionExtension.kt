package com.malek.giffy.utilities

import com.malek.giffy.R
import java.lang.Exception
import java.net.ConnectException
import java.net.SocketTimeoutException
import kotlin.random.Random

val gifError = arrayOf(R.raw.error_1, R.raw.error_2, R.raw.error_3)

fun Exception.formatError() =
    if (this is SocketTimeoutException || this is ConnectException) R.string.no_network_error else R.string.unexpected_error

fun Exception.getGIFError() =
    if (this is SocketTimeoutException || this is ConnectException) R.raw.no_internet_gif else randomErrorGif()


fun randomErrorGif(): Int {
    return gifError[Random.nextInt(0, gifError.size)]
}