package com.ssafy.fitmily_android.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ssafy.fitmily_android.presentation.ui.main.MainScreen

fun NavGraphBuilder.mainNavGraph() {
    navigation(
        startDestination = "main",
        route = RootNavGraph.MainNavGraph.route // mainNavGraph
    ) {
        composable("main") {
            MainScreen()
        }
    }
}