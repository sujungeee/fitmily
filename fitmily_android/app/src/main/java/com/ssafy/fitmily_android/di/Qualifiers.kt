package com.ssafy.fitmily_android.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WeatherRetrofit


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class S3Retrofit