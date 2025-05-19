package com.ssafy.fitmily_android.di

import com.ssafy.fitmily_android.model.service.AuthService
import com.ssafy.fitmily_android.model.service.ChatService
import com.ssafy.fitmily_android.model.service.FileService
import com.ssafy.fitmily_android.model.service.HomeService
import com.ssafy.fitmily_android.model.service.MyGoalService
import com.ssafy.fitmily_android.model.service.MyHealthService
import com.ssafy.fitmily_android.model.service.NotificationService
import com.ssafy.fitmily_android.model.service.S3Service
import com.ssafy.fitmily_android.model.service.WalkService
import com.ssafy.fitmily_android.model.service.WeatherService
import com.ssafy.fitmily_android.network.AccessTokenInterceptor
import com.ssafy.fitmily_android.network.ReissueInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Provider
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideReissueInterceptor(
        authService: Provider<AuthService>
    ): ReissueInterceptor {
        return ReissueInterceptor(authService)
    }

    @Singleton
    @Provides
    @MainRetrofit
    fun provideOkHttpClient(
        accessTokenInterceptor: AccessTokenInterceptor
        , reissueInterceptor: ReissueInterceptor
    ) : OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addNetworkInterceptor(accessTokenInterceptor)
            .authenticator(reissueInterceptor)
            .build()
    }

    @Singleton
    @Provides
    @MainRetrofit
    fun provideRetrofit(@MainRetrofit okHttpClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://k12d208.p.ssafy.io/api/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    @WeatherRetrofit
    fun provideWeatherOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            // 필요하다면 추가 Interceptor 가능
            .build()
    }

    @Singleton
    @Provides
    @WeatherRetrofit
    fun provideWeatherRetrofit(@WeatherRetrofit weatherOkHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .client(weatherOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    @S3Retrofit
    fun provideS3OkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    @Singleton
    @Provides
    @S3Retrofit
    fun provideS3Retrofit(@S3Retrofit provideS3OkHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://k12d208.p.ssafy.io/api/")
            .client(provideS3OkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthService(@MainRetrofit retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Singleton
    @Provides
    fun provideWalkService(@MainRetrofit retrofit: Retrofit): WalkService {
        return retrofit.create(WalkService::class.java)
    }

    @Singleton
    @Provides
    fun provideHomeService(@MainRetrofit retrofit: Retrofit): HomeService {
        return retrofit.create(HomeService::class.java)
    }

    @Singleton
    @Provides
    fun provideMyHealthService(@MainRetrofit retrofit: Retrofit): MyHealthService {
        return retrofit.create(MyHealthService::class.java)
    }

    @Singleton
    @Provides
    fun provideWeatherService(@WeatherRetrofit weatherRetrofit: Retrofit): WeatherService {
        return weatherRetrofit.create(WeatherService::class.java)
    }

    @Singleton
    @Provides
    fun provideChatService(@MainRetrofit retrofit: Retrofit): ChatService {
        return retrofit.create(ChatService::class.java)
    }

    @Singleton
    @Provides
    fun provideMyGoalService(@MainRetrofit retrofit: Retrofit): MyGoalService {
        return retrofit.create(MyGoalService::class.java)
    }

    @Singleton
    @Provides
    fun provideNotificationService(@MainRetrofit retrofit: Retrofit): NotificationService {
        return retrofit.create(NotificationService::class.java)
    }
    @Singleton
    @Provides
    fun provideFileService(@MainRetrofit retrofit: Retrofit): FileService {
        return retrofit.create(FileService::class.java)
    }

    @Singleton
    @Provides
    fun provideS3Service(@S3Retrofit retrofit: Retrofit): S3Service {
        return retrofit.create(S3Service::class.java)
    }
}