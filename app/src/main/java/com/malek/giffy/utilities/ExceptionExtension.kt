package com.malek.giffy.utilities

import com.malek.giffy.R
import java.lang.Exception
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.random.Random

private val gifError = arrayOf(R.raw.error_1, R.raw.error_2, R.raw.error_3)

fun Throwable.formatError() =
    if (this.isNoConnexionNetwork()) R.string.no_network_error else R.string.unexpected_error

fun Throwable.getGIFError() =
    if (this.isNoConnexionNetwork()) R.raw.no_internet_gif else randomErrorGif()

fun Throwable.isNoConnexionNetwork() =
    this is SocketTimeoutException || this is ConnectException || this is UnknownHostException

fun randomErrorGif(): Int {
    return gifError[Random.nextInt(0, gifError.size)]
}