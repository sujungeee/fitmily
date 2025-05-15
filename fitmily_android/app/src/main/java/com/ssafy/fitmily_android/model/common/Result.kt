package com.ssafy.fitmily_android.model.common

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
    object NetworkError : Result<Nothing>()
}
