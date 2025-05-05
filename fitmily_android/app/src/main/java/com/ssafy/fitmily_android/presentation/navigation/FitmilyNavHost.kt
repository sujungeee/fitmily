package com.ssafy.fitmily_android.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun FitmilyNavHost (
    startDestination : RootNavGraph
){
    val navController = rememberNavController()

    NavHost(
        navController = navController
        , startDestination = startDestination.route
    ) {
        authNavGraph(navController)
        mainNavGraph(navController)
    }
}

sealed class RootNavGraph(val route: String) {
    data object AuthNavGraph : RootNavGraph("authNavGraph")
    data object MainNavGraph : RootNavGraph("mainNavGraph")
}