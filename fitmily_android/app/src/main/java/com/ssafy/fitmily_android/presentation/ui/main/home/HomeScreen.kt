package com.ssafy.fitmily_android.presentation.ui.main.home


import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssafy.fitmily_android.BuildConfig
import com.ssafy.fitmily_android.presentation.ui.MainActivity
import com.ssafy.fitmily_android.presentation.ui.main.home.component.AloneHome
import com.ssafy.fitmily_android.presentation.ui.main.home.component.FamilyHome
import com.ssafy.fitmily_android.presentation.ui.main.walk.live.WalkLiveData
import com.ssafy.fitmily_android.util.LocationUtil
import androidx.activity.compose.rememberLauncherForActivityResult as rememberLauncherForActivityResult1

private const val TAG = "HomeScreen"
@Composable
fun HomeScreen(
    navController: NavHostController,
    homeVieModel: HomeViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val homeUiState by homeVieModel.homeUiState.collectAsState()

    var isInFamily = remember { mutableStateOf(true) }

    val requestPermissionLauncher = rememberLauncherForActivityResult1(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->

            val fineGranted = permissions[ACCESS_FINE_LOCATION] == true
            val coarseGranted = permissions[ACCESS_COARSE_LOCATION] == true

            // 권한 허용
            if(fineGranted && coarseGranted) {
                LocationUtil().getLastLocation(
                    context = context,
                    onLocationReceived = { lat, lon ->
                        Log.d("test1234", "lat : $lat lon : $lon")
                        homeVieModel.getWeatherInfo(lat, lon)
                    },
                    onFailure = {
                        Toast.makeText(context, "위치 정보를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                )
            }
            // 권한 거부
            else {
                Toast.makeText(context, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    )

    if (isInFamily.value) {
        FamilyHome(
            navController = navController,
            weather = homeUiState.weather
        )
    } else {
        AloneHome()
    }

    LaunchedEffect(Unit) {
        if(!hasLocationPermission(context)) {
            requestPermissionLauncher.launch(
                arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
            )
        }
        else {
            LocationUtil().getLastLocation(
                context,
                onLocationReceived = { lat, lon ->
                    Log.d("test1234", "lat : $lat lon : $lon")
                    homeVieModel.getWeatherInfo(lat, lon)
                },
                onFailure = {
                    Toast.makeText(context, "위치 정보를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

//    if(WalkLiveData.isServiceRunning.value) {
//        Log.d(TAG, "HomeScreen: ")
//        navController.navigate("walk/main")
//        Log.d(TAG, "HomeScreen: ${WalkLiveData.lat}, ${WalkLiveData.lon}, ${WalkLiveData.speed}, ${WalkLiveData.lastUpdatedTime}")
//    }

}

fun hasLocationPermission(context: Context): Boolean{
    return ContextCompat.checkSelfPermission(
        context,
        ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            &&
            ContextCompat.checkSelfPermission(
                context,
                ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
}





@Composable
@Preview(showSystemUi = true)
fun HomeScreenPreview() {
    HomeScreen(rememberNavController())
}