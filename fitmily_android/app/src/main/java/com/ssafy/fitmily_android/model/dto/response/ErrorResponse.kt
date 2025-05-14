package com.ssafy.fitmily_android.model.dto.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
)
