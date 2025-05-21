package com.ssafy.fitmily_android.model.dto.response.walk

data class WalkingFamilyResponse(
    val member : List<WalkingFamilyMember>,
){
    data class WalkingFamilyMember(
        val userFamilySequence: Int,
        val userId: Int,
        val userNickname: String,
        val userZodiacName: String,
    )
}