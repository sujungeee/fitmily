package com.ssafy.fitmily_android.presentation.ui.main.walk.history

import com.ssafy.fitmily_android.model.dto.response.walk.GpsDto
import com.ssafy.fitmily_android.model.dto.response.walk.HistoryDto
import com.ssafy.fitmily_android.model.dto.response.walk.WalkHistoryResponse
import com.ssafy.fitmily_android.model.dto.response.walk.WalkingFamilyResponse

data class WalkHistoryUiState(
    val isLoading: Boolean = false,
    val tstMessage: String = "",
    val walkHistoryResponse: WalkHistoryResponse = WalkHistoryResponse(
        walk = listOf(
            HistoryDto(
                walkId = 28,
                userId = 2,
                routeImg = "1747802129745",
                startTime = "2025-05-21T22:35:08",
                endTime = "2025-05-21T22:35:30",
                distance = 12.0,
                calories = 100,
                nickname = "ssafy123",
                zodiacName = "Rabbit",
                userFamilySequence = 1
            ),
            HistoryDto(
                walkId = 29,
                userId = 3,
                routeImg = "1747802129745",
                startTime = "2025-05-21T22:35:08",
                endTime = "2025-05-21T22:35:30",
                distance = 11.0,
                calories = 100,
                nickname = "김정모",
                zodiacName = "Rabbit",
                userFamilySequence = 2
            ),
            HistoryDto(
                walkId = 30,
                userId = 2,
                routeImg = "1747802129745",
                startTime = "2025-05-21T22:35:08",
                endTime = "2025-05-21T22:35:30",
                distance = 20.0,
                calories = 100,
                nickname = "test2",
                zodiacName = "Cow",
                userFamilySequence = 3
            ),
            HistoryDto(
                walkId = 30,
                userId = 2,
                routeImg = "1747802129745",
                startTime = "2025-05-21T22:35:08",
                endTime = "2025-05-21T22:35:30",
                distance = 1.0,
                calories = 100,
                nickname = "test2",
                zodiacName = "Cow",
                userFamilySequence = 3
            ),
            HistoryDto(
                walkId = 30,
                userId = 2,
                routeImg = "1747802129745",
                startTime = "2025-05-23T22:35:08",
                endTime = "2025-05-23T22:35:30",
                distance = 2.0,
                calories = 130,
                nickname = "테스트",
                zodiacName = "Dragon",
                userFamilySequence = 5
            ),
        )
    ),
)
