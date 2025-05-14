package com.ssafy.fitmily_android.model.dto.response

import com.google.gson.annotations.SerializedName

open class BaseResponse<T>(
    @SerializedName("success") val success: Boolean = true,
    @SerializedName("code") val code: Int = 200,
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: T? = null
)