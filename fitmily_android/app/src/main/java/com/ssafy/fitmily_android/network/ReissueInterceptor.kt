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

            val newRequest = runCatching {
                authService.reissue(ReissueRequest(refreshToken))
            }.fold(
                onSuccess = {
                    authDataStore.setAccessToken(it.accessToken)
                    authDataStore.setRefreshToken(it.refreshToken)

                    response.request.newBuilder()
                        .addHeader("Authorization", "Bearer ${it.accessToken}")
                        .build()
                }
                , onFailure = {
                    runBlocking { authDataStore.setAuthExpired(true) }
                    null
                }
            )

            return@runBlocking newRequest
        }
    }
}