package com.ssafy.fitmily_android.model.dto.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
){
    constructor() : this(
        code = 0,
        message = "다시 한번 시도해주세요"
    )

    constructor(message: String) : this(
        code = 0,
        message = message
    )
}
