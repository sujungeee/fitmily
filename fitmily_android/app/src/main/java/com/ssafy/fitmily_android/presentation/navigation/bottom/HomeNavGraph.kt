package com.ssafy.fitmily_android.presentation.navigation.bottom

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ssafy.fitmily_android.presentation.ui.main.home.HomeScreen

fun NavGraphBuilder.homeNavGraph() {
    navigation(
        startDestination = "home/main"
        , route = BottomNavItem.Home.route
    ) {
        composable("home/main") {
            HomeScreen()
        }

        // TODO
        composable("home/detail") {
            HomeScreen()
        }
    }
}