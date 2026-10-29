package com.aistudyos.app.core.common.extensions

import com.aistudyos.app.core.common.models.AppException
import com.aistudyos.app.core.common.models.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

fun <T> Flow<T>.asResult(): Flow<Result<T>> =
    this
        .map<T, Result<T>> { Result.Success(it) }
        .onStart { emit(Result.Loading) }
        .catch { e ->
            emit(Result.Error(AppException.UnknownException(e.message ?: "Unknown error")))
        }
