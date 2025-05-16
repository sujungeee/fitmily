package com.ssafy.fitmily_android.model.dto.response.chat

import com.google.gson.annotations.SerializedName

data class SenderInfo(
    @SerializedName("nickname") val userNickname: String
    , @SerializedName("familySequence") val familySequence: Int
    , @SerializedName("userZodiacName") val userZodiacName: String
)