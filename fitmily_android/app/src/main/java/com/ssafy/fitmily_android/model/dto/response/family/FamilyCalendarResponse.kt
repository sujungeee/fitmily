package com.ssafy.fitmily_android.model.dto.response.family

data class FamilyCalendarResponse(
    val calendar: List<FamilyCalendarCalendar>,
    val members: List<FamilyCalendarMember>
)