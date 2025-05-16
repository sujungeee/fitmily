package com.ssafy.fitmily_android.util

import com.ssafy.fitmily_android.model.common.Result

object ViewModelResultHandler {
    inline fun <T> handle(
        result: Result<T>,
        onSuccess: (T) -> Unit,
        onError: (String) -> Unit
    ) {
        when (result) {
            is Result.Success -> onSuccess(result.data)
            is Result.Error -> onError(result.error?.message ?: "알 수 없는 오류입니다.")
            is Result.NetworkError -> onError("네트워크 연결을 확인해주세요.")
        }
    }
}