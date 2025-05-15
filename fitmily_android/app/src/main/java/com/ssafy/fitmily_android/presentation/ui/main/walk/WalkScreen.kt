package com.ssafy.fitmily_android.presentation.ui.main.walk

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startForegroundService
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.PathOverlay
import com.naver.maps.map.compose.rememberFusedLocationSource
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.model.dto.GpsDto
import com.ssafy.fitmily_android.presentation.ui.main.MainScreen
import com.ssafy.fitmily_android.presentation.ui.main.home.component.ProfileItem
import com.ssafy.fitmily_android.presentation.ui.main.walk.component.StopWalkDialog
import com.ssafy.fitmily_android.presentation.ui.main.walk.live.WalkLiveData
import com.ssafy.fitmily_android.presentation.ui.main.walk.live.WalkLiveService
import com.ssafy.fitmily_android.ui.theme.mainBlue
import com.ssafy.fitmily_android.ui.theme.mainGray
import com.ssafy.fitmily_android.ui.theme.mainWhite
import com.ssafy.fitmily_android.ui.theme.secondaryBlue

private const val TAG = "WalkScreen"
@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun WalkScreen(
    navController: NavHostController
)  {

    val context = LocalContext.current

    var isWalking = remember { mutableStateOf(false) }
    var isDialogOpen = remember { mutableStateOf(false) }
    var watching = remember { mutableStateOf(0) }

    var path = remember { mutableStateOf(listOf<LatLng>()) }

    val locationSource = rememberFusedLocationSource()

    LaunchedEffect(WalkLiveData.gpsList.value) {
        Log.d(TAG, "WalkScreen: ${WalkLiveData.gpsList.value}")
        WalkLiveData.gpsList.value?.let { list ->
            path.value = list.map {
                LatLng(it.lat, it.lon)
            }
        }
    }
    var tmp =GpsDto(
        0.0,
        0.0,
        System.currentTimeMillis().toString(),
    )
    WalkLiveData.gpsList.observeForever(
        { list ->
            Log.d(TAG, "WalkScreen: $list")
            path.value = list.map {
                LatLng(it.lat, it.lon)
            }
            tmp =GpsDto(list.last().lat, list.last().lon, System.currentTimeMillis().toString())
        }
    )

    Column(
        modifier = Modifier
            .padding(
                top = 32.dp,
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 28.dp, end = 28.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "산책",
                style = typography.headlineMedium,
            )
            TextButton(onClick = {
                navController.navigate("walk/history")
            }) {
                Text(
                    text = "기록",
                    style = typography.titleMedium,
                    color = mainBlue,
                )
            }
        }
        if(isWalking.value){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 28.dp, end = 28.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("${tmp.lat}", style = typography.titleMedium)
                Text("거리", style = typography.bodyMedium, color = Color.Gray)
            }
            Spacer(
                modifier = Modifier
                    .width(1.dp)
                    .height(50.dp)
                    .background(mainGray)
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("${tmp.lon}", style = typography.titleMedium)
                Text("시간", style = typography.bodyMedium, color = Color.Gray)
            }
            Spacer(
                modifier = Modifier
                    .width(1.dp)
                    .height(60.dp)
                    .background(mainGray)
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("90", style = typography.titleMedium)
                Text("페이스", style = typography.bodyMedium, color = Color.Gray)
            }
        }
            }

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.TopEnd
        ) {
            NaverMap(
                modifier = Modifier
                    .fillMaxSize(),
                locationSource = locationSource,
                properties = MapProperties(
                    locationTrackingMode = LocationTrackingMode.Follow,
                ),
                uiSettings = MapUiSettings(
                    isLocationButtonEnabled = true,
                    isZoomControlEnabled = false,
                ),
            ){
                if (path.value.size>2) {
                    PathOverlay(
                        coords = path.value,
                        color = Color(0xFF3498DB),
                        width = 10.dp,
                    )
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(0.5f),
                horizontalAlignment = Alignment.End
            ) {
                items(5) { index ->
                    FilterChip(
                        modifier = Modifier
                            .padding(4.dp)
                            .height(26.dp),
                        onClick = {
                            watching.value = index
                        },
                        selected = if(index==watching.value) true else false,
                        label = {
                            ProfileItem(typography.bodySmall)
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = mainWhite,
                            labelColor = mainBlue,
                            selectedContainerColor = secondaryBlue,
                        ),
                    )
                }

            }

        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    if(isWalking.value){
                        isDialogOpen.value = true

                        //foregroundService 종료
                    }else {
                        isWalking.value = !isWalking.value
                        //foregroundService 시작
                        WalkLiveData.startWalkLiveService(context)
                    }},
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3498DB)),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.size(80.dp)
            ) {
                if (!isWalking.value) {
                    Icon(
                        painter = painterResource(id = R.drawable.walk_start_icon), // 정지 아이콘
                        contentDescription = "정지",
                        tint = Color.White,
                        modifier = Modifier.size(70.dp),
                    )
                }else {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(mainWhite)
                            .wrapContentSize(Alignment.Center)
                    )
                }
            }

        }


    }
    if (isDialogOpen.value) {
        StopWalkDialog(
            onDismissRequest = { isDialogOpen.value = false },
            onConfirmation = {
                isWalking.value = !isWalking.value
                isDialogOpen.value = false
                WalkLiveData.stopWalkLiveService(context)
            }
        )
    }
}


@Composable
@Preview(showSystemUi = true)
fun WalkScreenPreview() {
    WalkScreen(rememberNavController())
}