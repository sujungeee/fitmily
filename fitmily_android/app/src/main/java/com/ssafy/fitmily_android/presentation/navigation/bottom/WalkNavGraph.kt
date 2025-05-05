package com.ssafy.fitmily_android.presentation.navigation.bottom

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ssafy.fitmily_android.presentation.ui.main.walk.WalkScreen

fun NavGraphBuilder.walkNavGraph(navController: NavHostController) {
    navigation(
        startDestination = "walk/main"
        , route = BottomNavItem.Walk.route
    ) {
        composable("walk/main") {
            WalkScreen(navController)
        }

        // TODO
        composable("walk/detail") {
            WalkScreen(navController)
        }
    }
}