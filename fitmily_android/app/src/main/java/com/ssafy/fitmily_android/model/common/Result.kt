package com.ssafy.fitmily_android.model.common

import com.ssafy.fitmily_android.model.dto.response.ErrorResponse

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val error: ErrorResponse? = null, val exception: Throwable? = null) : Result<Nothing>()
    object NetworkError : Result<Nothing>()
}
