package com.ssafy.fitmily_android

import android.app.Application
import com.ssafy.fitmily_android.util.datastore.AuthDataStore
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {
    private lateinit var authDataStore: AuthDataStore

    companion object {
        private lateinit var instance: MainApplication
        fun getInstance(): MainApplication {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        authDataStore = AuthDataStore(this)
    }

    fun getDataStore(): AuthDataStore {
        return authDataStore
    }
}