package com.ssafy.fitmily_android.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.ssafy.fitmily_android.MainApplication
import com.ssafy.fitmily_android.presentation.navigation.FitmilyNavHost
import com.ssafy.fitmily_android.presentation.navigation.RootNavGraph
import com.ssafy.fitmily_android.ui.theme.FitmilyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authDataStore = MainApplication.getInstance().getDataStore()

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
//                startGraph?.let {
//                    FitmilyNavHost(navController, it)
//                }
                FitmilyNavHost(navController, RootNavGraph.MainNavGraph) // TODO: 통신 성공 시 delete
            }
        }
    }
}