package com.ssafy.fitmily_android.util.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "shared_data")
class AuthDataStore(private val context: Context){

    // key
    val accessToken = stringPreferencesKey("accessToken")
    val refreshToken = stringPreferencesKey("refreshToken")
    val familyId = intPreferencesKey("familyId")
    val userId = intPreferencesKey("userId")
    val userNickname = stringPreferencesKey("userNickname")
    val userZodiacName = stringPreferencesKey("userZodiacName")
    val authExpired = booleanPreferencesKey("authExpired")

    // clear
    suspend fun clear() {
        context.datastore.edit {
            it.clear()
        }
    }

    // set
    suspend fun setAccessToken(value: String) {
        context.datastore.edit {
            it[accessToken] = value
        }
    }

    suspend fun setRefreshToken(value: String) {
        context.datastore.edit {
            it[refreshToken] = value
        }

    }

    suspend fun setFamilyId(value: Int) {
        context.datastore.edit {
            it[familyId] = value
        }
    }

    suspend fun setUserId(value: Int) {
        context.datastore.edit {
            it[userId] = value
        }
    }

    suspend fun setUserNickname(value: String) {
        context.datastore.edit {
            it[userNickname] = value
        }
    }

    suspend fun setUserZodiacName(value: String) {
        context.datastore.edit {
            it[userZodiacName] = value
        }
    }

    // get
    suspend fun getAccessToken(): String {
        return context.datastore.data.first()[accessToken] ?: ""
    }

    suspend fun getRefreshToken(): String {
        return context.datastore.data.first()[refreshToken] ?: ""
    }

    suspend fun getFamilyId(): Int {
        return context.datastore.data.first()[familyId] ?: -1
    }

    suspend fun getUserId(): Int {
        return context.datastore.data.first()[userId] ?: -1
    }

    suspend fun getUserNickname(): String {
        return context.datastore.data.first()[userNickname] ?: ""
    }

    suspend fun getUserZodiacName(): String {
        return context.datastore.data.first()[userZodiacName] ?: ""
    }

    // reissue
    suspend fun setAuthExpired(value: Boolean) {
        context.datastore.edit {
            it[authExpired] = value
        }
    }

    fun authExpiredFlow(): Flow<Boolean> {
        return context.datastore.data.map {
            it[authExpired] ?: false
        }
    }
}