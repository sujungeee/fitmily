package com.ssafy.fitmily_android.presentation.navigation.bottom

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ssafy.fitmily_android.presentation.ui.main.family.FamilyScreen
import com.ssafy.fitmily_android.presentation.ui.main.family.detail.FamilyDetailScreen

fun NavGraphBuilder.familyNavGraph(navController: NavHostController) {
    navigation(
        startDestination = "family/main"
        , route = BottomNavItem.Family.route
    ) {
        composable("family/main") {
            FamilyScreen(navController)
        }

        // TODO
        composable("family/main/detail") {
            FamilyDetailScreen(navController)
        }
    }
}