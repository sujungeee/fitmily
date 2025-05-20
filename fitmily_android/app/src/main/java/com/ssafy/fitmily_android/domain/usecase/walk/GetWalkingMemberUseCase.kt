package com.ssafy.fitmily_android.domain.usecase.walk

import com.ssafy.fitmily_android.domain.repository.WalkRepository
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.response.walk.WalkPathResponse
import com.ssafy.fitmily_android.model.dto.response.walk.WalkingFamilyResponse
import javax.inject.Inject

class GetWalkingMemberUseCase @Inject constructor(
    private val walkRepository: WalkRepository
) {
    suspend operator fun invoke(familyId: Int): Result<WalkingFamilyResponse> {
        return walkRepository.getWalkingMembers(familyId)
    }
}


