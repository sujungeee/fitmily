package com.ssafy.fitmily_android.presentation.ui.main.home


import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssafy.fitmily_android.presentation.ui.main.home.component.AloneHome
import com.ssafy.fitmily_android.presentation.ui.main.home.component.FamilyHome
import com.ssafy.fitmily_android.presentation.ui.main.walk.live.WalkLiveData

private const val TAG = "HomeScreen"
@Composable
fun HomeScreen(
    navController: NavHostController
) {
    var isInFamily = remember { mutableStateOf(true) }

    if (isInFamily.value) {
        FamilyHome(navController)
    } else {
        AloneHome()
    }

    if(WalkLiveData.isServiceRunning.value) {
        Log.d(TAG, "HomeScreen: ")
        navController.navigate("walk/main")
        Log.d(TAG, "HomeScreen: ${WalkLiveData.lat}, ${WalkLiveData.lon}, ${WalkLiveData.speed}, ${WalkLiveData.lastUpdatedTime}")
    }

}






@Composable
@Preview(showSystemUi = true)
fun HomeScreenPreview() {
    HomeScreen(rememberNavController())
}