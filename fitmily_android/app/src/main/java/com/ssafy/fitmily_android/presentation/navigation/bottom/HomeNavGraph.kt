package com.ssafy.fitmily_android.presentation.navigation.bottom

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.google.gson.Gson
import com.ssafy.fitmily_android.model.dto.response.walk.HistoryDto
import com.ssafy.fitmily_android.presentation.ui.main.home.HomeScreen
import com.ssafy.fitmily_android.presentation.ui.main.home.profile.FamilyProfileScreen
import com.ssafy.fitmily_android.presentation.ui.main.walk.history.WalkHistoryDetailScreen

fun NavGraphBuilder.homeNavGraph(navController: NavHostController) {
    navigation(
        startDestination = "home/main"
        , route = BottomNavItem.Home.route
    ) {
        composable("home/main") {
            HomeScreen(navController)
        }
        composable("home/family/{familyId}") { backStackEntry ->
            val familyId = backStackEntry.arguments?.getString("familyId")?.toIntOrNull()?:0
            FamilyProfileScreen(navController, familyId)
        }



    }
}