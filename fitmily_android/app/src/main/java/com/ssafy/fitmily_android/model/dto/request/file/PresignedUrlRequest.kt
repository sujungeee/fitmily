package com.ssafy.fitmily_android.model.dto.request.file

import com.google.gson.annotations.SerializedName

data class PresignedUrlRequest (
    @SerializedName("filename") val fileName: String
    , @SerializedName("contenttype") val contentType: String
)