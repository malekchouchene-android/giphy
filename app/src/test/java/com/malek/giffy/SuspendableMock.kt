package com.malek.giffy

import com.malek.giffy.domaine.Result

interface SuspendableMock<T> {
    fun suspendFunctionMock(): Result<T>
}