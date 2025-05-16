package com.ssafy.fitmily_android.model.dto.response.home

data class FamilyResponse(
    val familyInviteCode: String,
    val familyName: String,
    val familyPeople : Int,
){
    constructor() : this(
        familyInviteCode = "",
        familyName = "",
        familyPeople = 0
    )
}