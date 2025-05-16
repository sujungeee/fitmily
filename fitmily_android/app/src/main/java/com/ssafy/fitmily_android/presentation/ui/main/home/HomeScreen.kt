package com.ssafy.fitmily_android.presentation.ui.main.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssafy.fitmily_android.MainApplication
import com.ssafy.fitmily_android.presentation.ui.main.home.component.AloneHome
import com.ssafy.fitmily_android.presentation.ui.main.home.component.FamilyHome

private const val TAG = "HomeScreen"
@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val authDataStore = MainApplication.getInstance().getDataStore()
    val homeUiState by homeViewModel.uiState.collectAsState()

    if (homeUiState.familyId!=0) {
        FamilyHome(navController)
    } else {
        AloneHome()
    }

//    if(WalkLiveData.isServiceRunning.value) {
//        Log.d(TAG, "HomeScreen: ")
//        navController.navigate("walk/main")
//        Log.d(TAG, "HomeScreen: ${WalkLiveData.lat}, ${WalkLiveData.lon}, ${WalkLiveData.speed}, ${WalkLiveData.lastUpdatedTime}")
//    }

}






@Composable
@Preview(showSystemUi = true)
fun HomeScreenPreview() {
    HomeScreen(rememberNavController())
}