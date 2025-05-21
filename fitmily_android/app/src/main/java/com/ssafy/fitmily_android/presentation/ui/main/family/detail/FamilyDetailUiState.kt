package com.ssafy.fitmily_android.presentation.ui.main.family.detail

import com.ssafy.fitmily_android.model.dto.response.family.FamilyDailyResponse

data class FamilyDetailUiState (
    val familyDailyResponse: FamilyDailyResponse? = null,
    val familyDetailSideEffect: FamilyDetailSideEffect? = null
)

sealed interface FamilyDetailSideEffect {
    data object NavigateToFamilyExercise: FamilyDetailSideEffect
}