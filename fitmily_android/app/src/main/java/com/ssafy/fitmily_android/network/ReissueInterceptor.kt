package com.ssafy.fitmily_android.network

import com.ssafy.fitmily_android.MainApplication
import com.ssafy.fitmily_android.model.dto.request.ReissueRequest
import com.ssafy.fitmily_android.model.service.AuthService
import jakarta.inject.Inject
import jakarta.inject.Provider
import javax.inject.Singleton
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

@Singleton
class ReissueInterceptor @Inject constructor(
    private val authServiceProvider: Provider<AuthService>
) : Authenticator {
    val authDataStore = MainApplication.getInstance().getDataStore()

    override fun authenticate(route: Route?, response: Response): Request? {
        val authService = authServiceProvider.get()
        val isPathReissue = response.request.url.encodedPath.contains("/reissue")
        if (isPathReissue) {
            runBlocking { authDataStore.setAuthExpired(true) }
            return null
        }

        return runBlocking {
            val refreshToken = authDataStore.getRefreshToken()

            val result = runCatching {
                authService.reissue(ReissueRequest(refreshToken))
            }
            result.fold(
                onSuccess = { tokenResponse ->
                    authDataStore.setAccessToken(tokenResponse.body()!!.accessToken)
                    authDataStore.setRefreshToken(tokenResponse.body()!!.refreshToken)

                    // 기존 요청에 새 토큰 추가
                    response.request.newBuilder()
                        .removeHeader("Authorization")
                        .addHeader("Authorization", "Bearer ${tokenResponse.body()!!.accessToken}")
                        .build()
                },
                onFailure = {
                    authDataStore.setAuthExpired(true)
                    null
                }
            )
        }
    }
}