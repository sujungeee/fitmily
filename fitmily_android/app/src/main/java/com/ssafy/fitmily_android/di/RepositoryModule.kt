package com.ssafy.fitmily_android.di

import com.ssafy.fitmily_android.domain.repository.AuthRepository
import com.ssafy.fitmily_android.model.repositoryimpl.AuthRepositoryImpl
import com.ssafy.fitmily_android.model.service.AuthService
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

    // TODO: add
}