package com.ssafy.fitmily_android.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.naver.maps.map.NaverMapSdk
import com.ssafy.fitmily_android.BuildConfig
import com.ssafy.fitmily_android.MainApplication
import com.ssafy.fitmily_android.presentation.navigation.FitmilyNavHost
import com.ssafy.fitmily_android.presentation.navigation.RootNavGraph
import com.ssafy.fitmily_android.ui.theme.FitmilyTheme
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authDataStore = MainApplication.getInstance().getDataStore()
        val fcmType = intent.getStringExtra("type")
        val fcmId = intent.getStringExtra("id")
        Log.d(TAG, "onCreate: fcmType: ${fcmType}, fcmId: ${fcmId}")

        setContent {
            val navController = rememberNavController()
            var startGraph by remember { mutableStateOf<RootNavGraph?>(null) }

            LaunchedEffect(Unit) {
                val accessToken = authDataStore.getAccessToken()
                startGraph = if (accessToken.isBlank()) {
                    RootNavGraph.AuthNavGraph
                } else {
                    RootNavGraph.MainNavGraph
                }
            }

            LaunchedEffect(true) {
                authDataStore.authExpiredFlow().collect { isExpired ->
                    if (isExpired) {
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                        authDataStore.setAuthExpired(false)
                    }
                }
            }

            FitmilyTheme {
                startGraph?.let {
                    FitmilyNavHost(navController, it, fcmType, fcmId)
                }
            }
        }
        Log.d(TAG, "onCreate: ${BuildConfig.NAVER_CLIENT_ID}")
        NaverMapSdk.getInstance(this).client = NaverMapSdk.NcpKeyClient(BuildConfig.NAVER_CLIENT_ID)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
}