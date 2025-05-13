package com.ssafy.fitmily_android.network

import com.ssafy.fitmily_android.MainApplication
import jakarta.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

@Singleton
class AccessTokenInterceptor @Inject constructor() : Interceptor {
    val authDataStore = MainApplication.getInstance().getDataStore()

    override fun intercept(chain: Interceptor.Chain): Response {
        return runBlocking {
            val accessToken = authDataStore.getAccessToken()
            val request = if (accessToken.isNotEmpty()) {
                chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $accessToken")
                    .build()
            } else {
                chain.request()
            }
            chain.proceed(request)
        }
    }
}