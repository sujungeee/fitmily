package com.ssafy.fitmily_android.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun FitmilyNavHost (
    navController: NavHostController
    , startDestination : RootNavGraph
    , fcmType: String?
    , fcmId: String?
){
    NavHost(
        navController = navController
        , startDestination = startDestination.route
    ) {
        authNavGraph(navController)
        mainNavGraph(navController, fcmType, fcmId)
    }
}

sealed class RootNavGraph(val route: String) {
    data object AuthNavGraph : RootNavGraph("authNavGraph")
    data object MainNavGraph : RootNavGraph("mainNavGraph")
}