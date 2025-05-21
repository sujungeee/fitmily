package com.ssafy.fitmily_android.model.dto.response.walk

data class WalkingFamilyResponse(
    val member : List<WalkingFamilyMember>,
){
    data class WalkingFamilyMember(
        val familySequence: Int,
        val familyUserId: Int,
        val familyUserNickname: String,
        val familyUserZodiacName: String,
        val familyUserProfileImage: String
    )
}