package com.ssafy.fitmily_android.di

import com.ssafy.fitmily_android.domain.repository.AuthRepository
import com.ssafy.fitmily_android.domain.repository.MyHealthRepository
import com.ssafy.fitmily_android.model.repositoryimpl.AuthRepositoryImpl
import com.ssafy.fitmily_android.model.repositoryimpl.MyHealthRepositoryImpl
import com.ssafy.fitmily_android.model.service.AuthService
import com.ssafy.fitmily_android.model.service.MyHealthService
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
}