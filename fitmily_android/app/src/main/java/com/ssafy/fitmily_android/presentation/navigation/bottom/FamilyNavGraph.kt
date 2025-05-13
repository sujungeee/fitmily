package com.ssafy.fitmily_android.presentation.navigation.bottom

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ssafy.fitmily_android.presentation.ui.main.family.FamilyScreen
import com.ssafy.fitmily_android.presentation.ui.main.family.detail.FamilyDetailScreen
import com.ssafy.fitmily_android.presentation.ui.main.family.exercise.FamilyExerciseScreen

fun NavGraphBuilder.familyNavGraph(navController: NavHostController) {
    navigation(
        startDestination = "family/main"
        , route = BottomNavItem.Family.route
    ) {
        composable("family/main") {
            FamilyScreen(navController)
        }

        composable("family/detail") {
            FamilyDetailScreen(navController)
        }

        composable("family/exercise") {
            FamilyExerciseScreen(navController)
        }
    }
}