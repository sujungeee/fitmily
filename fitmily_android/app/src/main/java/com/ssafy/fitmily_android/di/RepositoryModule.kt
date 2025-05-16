package com.ssafy.fitmily_android.di

import com.ssafy.fitmily_android.domain.repository.AuthRepository
import com.ssafy.fitmily_android.domain.repository.ChatRepository
import com.ssafy.fitmily_android.domain.repository.MyHealthRepository
import com.ssafy.fitmily_android.domain.repository.WeatherRepository
import com.ssafy.fitmily_android.model.repositoryimpl.AuthRepositoryImpl
import com.ssafy.fitmily_android.model.repositoryimpl.ChatRepositoryImpl
import com.ssafy.fitmily_android.model.repositoryimpl.MyHealthRepositoryImpl
import com.ssafy.fitmily_android.model.repositoryimpl.WeatherRepositoryImpl
import com.ssafy.fitmily_android.model.service.AuthService
import com.ssafy.fitmily_android.model.service.ChatService
import com.ssafy.fitmily_android.model.service.MyHealthService
import com.ssafy.fitmily_android.model.service.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun providesAuthRepository(
        authService: AuthService
    ): AuthRepository {
        return AuthRepositoryImpl(authService)
    }

    @Singleton
    @Provides
    fun providesMyHealthRepository(
        myHealthService: MyHealthService
    ): MyHealthRepository {
        return MyHealthRepositoryImpl(myHealthService)
    }

    @Singleton
    @Provides
    fun providesWeatherRepository(
        weatherService: WeatherService
    ): WeatherRepository {
        return WeatherRepositoryImpl(weatherService)
    }
    @Singleton
    @Provides
    fun providesChatRepository(
        chatService: ChatService
    ): ChatRepository {
        return ChatRepositoryImpl(chatService)
    }

    // TODO: add
}