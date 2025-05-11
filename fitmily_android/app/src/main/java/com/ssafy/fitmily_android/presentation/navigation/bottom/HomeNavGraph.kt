package com.ssafy.fitmily_android.presentation.navigation.bottom

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ssafy.fitmily_android.presentation.ui.main.home.HomeScreen
import com.ssafy.fitmily_android.presentation.ui.main.home.challenge.ChallengeScreen
import com.ssafy.fitmily_android.presentation.ui.main.home.profile.FamilyProfileScreen

fun NavGraphBuilder.homeNavGraph(navController: NavHostController) {
    navigation(
        startDestination = "home/main"
        , route = BottomNavItem.Home.route
    ) {
        composable("home/main") {
            HomeScreen(navController)
        }
        composable("home/family") {
            FamilyProfileScreen(navController)
        }
        composable("home/challenge") {
            ChallengeScreen(navController)
        }



    }
}