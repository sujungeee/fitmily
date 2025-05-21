package com.ssafy.fitmily_android.domain.repository

import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.request.home.FamilyCreateRequest
import com.ssafy.fitmily_android.model.dto.request.home.FamilyJoinRequest
import com.ssafy.fitmily_android.model.dto.response.home.ChallengeResponse
import com.ssafy.fitmily_android.model.dto.response.home.FamilyHealthResponse
import com.ssafy.fitmily_android.model.dto.response.home.FamilyJoinResponse
import com.ssafy.fitmily_android.model.dto.response.home.FamilyResponse
import com.ssafy.fitmily_android.model.dto.response.home.FamilyTodayResponse

interface HomeRepository {

    suspend fun createFamily(request: FamilyCreateRequest): Result<FamilyJoinResponse>

    suspend fun joinFamily(request: FamilyJoinRequest): Result<FamilyJoinResponse>

    suspend fun getFamily(familyId: Int): Result<FamilyResponse>

    suspend fun getDashboard(familyId: Int): Result<FamilyTodayResponse>

    suspend fun getFamilyHealth(familyId: Int): Result<FamilyHealthResponse>

    suspend fun getChallenge(): Result<ChallengeResponse>

    suspend fun sendPoke(userId: Int): Result<Unit>
}