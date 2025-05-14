package com.ssafy.fitmily_android.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun FitmilyNavHost (
    navController: NavHostController
    , startDestination : RootNavGraph
){
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