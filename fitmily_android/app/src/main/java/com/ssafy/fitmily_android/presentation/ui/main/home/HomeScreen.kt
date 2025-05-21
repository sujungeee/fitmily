package com.ssafy.fitmily_android.presentation.ui.main.home


import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.presentation.ui.main.home.component.AloneHome
import com.ssafy.fitmily_android.presentation.ui.main.home.component.FamilyHome
import com.ssafy.fitmily_android.ui.theme.mainGray
import com.ssafy.fitmily_android.util.LocationUtil
import androidx.activity.compose.rememberLauncherForActivityResult as rememberLauncherForActivityResult1

private const val TAG = "HomeScreen"
@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val homeUiState by homeViewModel.uiState.collectAsState()

    val isLoading = remember { mutableStateOf(false) }


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
                        homeViewModel.getWeatherInfo(lat, lon)
                    },
                    onFailure = {
                        Toast.makeText(context, "위치 정보를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                )
            }
            else {
                Toast.makeText(context, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    )

    Box(){
    if (homeUiState.familyId != 0 && homeUiState.familyId != 100) {
        FamilyHome(
            navController = navController,
            homeUiState = homeUiState,
            onClickPoke = {
                homeViewModel.sendPoke(it)
            },

        )
    } else if (homeUiState.familyId == 0) {
        AloneHome(
            onClickCreate = {
                homeViewModel.createFamily(it)
            },
            onClickJoin = {
                homeViewModel.joinFamily(it)
            },
        )
    }
//    else{
//        Box(
//            modifier = Modifier.fillMaxSize().background(Color(0x33000000)),
//            contentAlignment = Alignment.Center
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.completed_icon),
//                contentDescription = null,
//            )
//        }
//    }
    }

    LaunchedEffect(Unit){
        homeViewModel.getFamilyId()
    }
//    LaunchedEffect(homeUiState.familyId) {
//        if(homeUiState.familyId!=0 && homeUiState.familyId!=100) {
//            homeViewModel.getFamily()
//            homeViewModel.getChallenge()
//            homeViewModel.getDashboard()
//        }
//    }

    LaunchedEffect(homeUiState.isLoading) {
        if (homeUiState.isLoading) {
            isLoading.value = true
        }else{
            isLoading.value = false
        }
    }

    LaunchedEffect(homeUiState.familyId) {
        if(homeUiState.familyId!=0 && homeUiState.familyId!=100) {
            homeViewModel.loadAllHomeData()
        }
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
                    homeViewModel.getWeatherInfo(lat, lon)
                },
                onFailure = {
                    Toast.makeText(context, "위치 정보를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

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