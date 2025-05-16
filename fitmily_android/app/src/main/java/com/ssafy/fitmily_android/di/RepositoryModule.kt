package com.ssafy.fitmily_android.di

import com.ssafy.fitmily_android.domain.repository.AuthRepository
import com.ssafy.fitmily_android.domain.repository.MyHealthRepository
import com.ssafy.fitmily_android.domain.repository.HomeRepository
import com.ssafy.fitmily_android.domain.repository.WalkRepository
import com.ssafy.fitmily_android.model.repositoryimpl.AuthRepositoryImpl
import com.ssafy.fitmily_android.model.repositoryimpl.HomeRepositoryImpl
import com.ssafy.fitmily_android.model.repositoryimpl.WalkRepositoryImpl
import com.ssafy.fitmily_android.model.repositoryimpl.MyHealthRepositoryImpl
import com.ssafy.fitmily_android.model.service.AuthService
import dagger.Binds
import com.ssafy.fitmily_android.model.service.MyHealthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindWalkRepository(
        walkRepositoryImpl: WalkRepositoryImpl
    ): WalkRepository

    @Binds
    @Singleton
    abstract fun bindHomeRepository(
        homeRepositoryImpl: HomeRepositoryImpl
    ): HomeRepository

    @Binds
    @Singleton
    abstract fun bindMyHealthRepository(
        myHealthRepositoryImpl: MyHealthRepositoryImpl
    ): MyHealthRepository
}