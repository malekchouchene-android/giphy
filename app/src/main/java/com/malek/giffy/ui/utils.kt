package com.malek.giffy.ui

import com.malek.giffy.R
import java.lang.Exception
import java.net.ConnectException
import java.net.SocketTimeoutException

fun Exception.formatError()= if (this is SocketTimeoutException || this is ConnectException) R.string.no_network_error else R.string.unexpected_error
