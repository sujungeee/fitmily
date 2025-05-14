package com.ssafy.fitmily_android.model.dto.response

import com.google.gson.annotations.SerializedName

open class BaseResponse<T>(
    @SerializedName("success") val success: Boolean = true,
    @SerializedName("data") val data: T? = null
)