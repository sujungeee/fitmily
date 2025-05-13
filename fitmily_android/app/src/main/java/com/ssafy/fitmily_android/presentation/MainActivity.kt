package com.ssafy.fitmily_android.presentation.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.naver.maps.map.NaverMapSdk
import com.ssafy.fitmily_android.BuildConfig
import com.ssafy.fitmily_android.presentation.navigation.FitmilyNavHost
import com.ssafy.fitmily_android.presentation.navigation.RootNavGraph
import com.ssafy.fitmily_android.ui.theme.FitmilyTheme

private const val TAG = "MainActivity"
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitmilyTheme {
                // TODO: graph는 로그인 여부에 따라 추후에 달라질 예정(일단 Main으로 바로 navigate)
                FitmilyNavHost(RootNavGraph.MainNavGraph)
            }
        }
                Log.d(TAG, "onCreate: ${BuildConfig.NAVER_CLIENT_ID}")
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NcpKeyClient(BuildConfig.NAVER_CLIENT_ID)

    }
}