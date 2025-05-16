package com.ssafy.fitmily_android.model.common

import com.google.gson.Gson
import com.ssafy.fitmily_android.model.dto.response.ErrorResponse
import retrofit2.Response
import java.io.IOException

object ApiResultHandler {
    inline fun <T> handleApi(call: () -> Response<T>): Result<T> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                Result.Success(body)
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = try {
                    Gson().fromJson(errorBody, ErrorResponse::class.java)
                } catch (e: Exception) {
                    ErrorResponse(message = "에러 바디 파싱 실패")
                }
                Result.Error(errorResponse)
            }
        } catch (e: IOException) {
            Result.NetworkError
        } catch (e: Exception) {
            Result.Error(ErrorResponse(message = e.message ?: "알 수 없는 오류"))
        }
    }
}
