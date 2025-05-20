package com.ssafy.fitmily_android.presentation.ui.main.walk

import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.PathOverlay
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.model.dto.request.walk.WalkEndRequest
import com.ssafy.fitmily_android.model.dto.response.walk.GpsDto
import com.ssafy.fitmily_android.presentation.ui.main.home.component.ProfileItem
import com.ssafy.fitmily_android.presentation.ui.main.walk.component.StopWalkDialog
import com.ssafy.fitmily_android.presentation.ui.main.walk.live.WalkLiveData
import com.ssafy.fitmily_android.presentation.ui.main.walk.live.WalkMapCaptureHelper
import com.ssafy.fitmily_android.ui.theme.mainBlue
import com.ssafy.fitmily_android.ui.theme.mainGray
import com.ssafy.fitmily_android.ui.theme.mainWhite
import com.ssafy.fitmily_android.ui.theme.secondaryBlue
import com.ssafy.fitmily_android.util.LocationUtil
import java.io.ByteArrayOutputStream
import java.time.Instant
import java.time.ZoneId
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

private const val TAG = "WalkScreen"
@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun WalkScreen(
    navController: NavHostController,
    walkViewModel: WalkViewModel = hiltViewModel(),
)  {

    val context = LocalContext.current



    var isWalking = remember { mutableStateOf(false) }
    var isDialogOpen = remember { mutableStateOf(false) }
    var watching = remember { mutableStateOf(0) }

    var path = remember { mutableStateOf(listOf<LatLng>()) }

    val elapsedTime = remember { mutableStateOf(0L) }
    val totalDistance = remember { mutableStateOf(0.0) }
    LaunchedEffect(WalkLiveData.gpsList.value) {
        Log.d(TAG, "WalkScreen: ${WalkLiveData.gpsList.value}")
        WalkLiveData.gpsList.value?.let { list ->
            path.value = list.map {
                LatLng(it.lat, it.lon)
            }
            totalDistance.value = calculateTotalDistance(path.value)
        }
    }

    LaunchedEffect(WalkLiveData.isServiceRunning) {
        isWalking.value = WalkLiveData.isServiceRunning.value
    }

    LaunchedEffect(isWalking.value) {
        if (isWalking.value) {
            while (true) {
                val now = System.currentTimeMillis()
                val started = WalkLiveData.startedTime // Long 값 (ms)

                if (started != 0L) {
                    elapsedTime.value = (now - started) / 1000L // 초 단위
                }

                kotlinx.coroutines.delay(1000L)
            }
        } else {
            elapsedTime.value = 0L
        }
    }


    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        LocationUtil().getLastLocation(context,
            onLocationReceived = { lat, lon ->
                position = CameraPosition(LatLng(lat, lon), 15.0)
            },
            onFailure = {
                Log.d(TAG, "WalkScreen: 실패")
            }
        )
    }

    WalkLiveData.gpsList.observeForever(
        { list ->
            if (list.isNotEmpty()) {
                path.value = list.map {
                    LatLng(it.lat, it.lon)
                }
            }
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
                Text("${String.format("%.2f m", totalDistance.value)}", style = typography.titleMedium)
                Text("거리", style = typography.bodyMedium, color = Color.Gray)
            }
            Spacer(
                modifier = Modifier
                    .width(1.dp)
                    .height(50.dp)
                    .background(mainGray)
            )
            val minutes = elapsedTime.value / 60
            val seconds = elapsedTime.value % 60

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(String.format("%02d:%02d", minutes, seconds), style = typography.titleMedium)
                Text("시간", style = typography.bodyMedium, color = Color.Gray)
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
                locationSource = rememberFusedLocationSource(),
                properties = MapProperties(
                    locationTrackingMode = LocationTrackingMode.Follow,
                ),
                uiSettings = MapUiSettings(
                    isLocationButtonEnabled = true,
                    isZoomControlEnabled = false,
                ),
                cameraPositionState = cameraPositionState,
                onMapClick= { _, it ->
                    cameraPositionState.move(
                        CameraUpdate.scrollTo(LatLng(it.latitude, it.longitude))
                    )
                },

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
                items(1) { index ->
                    FilterChip(
                        modifier = Modifier
                            .padding(4.dp)
                            .height(26.dp),
                        onClick = {
                            watching.value = index
                        },
                        selected = if(index==watching.value) true else false,
                        label = {
                            ProfileItem(typography.bodySmall, 1, "내 산책", "mouse",)
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = mainWhite,
                            labelColor = mainBlue,
                            selectedContainerColor = secondaryBlue,
                        ),
                    )
                }

            }

            Row(modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom) {
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


    }
    if (isDialogOpen.value) {
        StopWalkDialog(
            onDismissRequest = { isDialogOpen.value = false },
            onConfirmation = {
                isWalking.value = !isWalking.value
                isDialogOpen.value = false
                WalkLiveData.stopWalkLiveService(context)

                // 캡처 추가
                if (path.value.isNotEmpty()) {
                    WalkMapCaptureHelper(
                        context = context,
                        path = path.value,
                        onCaptured = { bitmap ->
                            bitmap?.let {
                                // 저장하거나 공유 처리, 예: 파일로 저장
                                bitmapToByteArray(it)
                                walkViewModel.postWalk(
                                    WalkEndRequest(
                                        walkDistance = totalDistance.value,
                                        walkStartTime = Instant.ofEpochMilli(WalkLiveData.startedTime)
                                            .atZone(ZoneId.systemDefault())
                                            .toLocalDateTime().toString(),
                                        walkEndTime = Instant.ofEpochMilli(System.currentTimeMillis())
                                            .atZone(ZoneId.systemDefault())
                                            .toLocalDateTime().toString(),
                                        walkRouteImg = System.currentTimeMillis().toString(),
                                    ),
                                    byteArray=bitmapToByteArray(it)
                                )
                            }
                        }
                    ).capture()
                }

                // 초기화
                WalkLiveData.gpsList.value = listOf()
                Toast.makeText(context, "산책이 종료되었습니다.", Toast.LENGTH_SHORT).show()
            }

        )
    }
}

fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val R = 6371000.0 // 지구 반지름 (단위: m)
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = sin(dLat / 2).pow(2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(dLon / 2).pow(2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return R * c // 결과 단위: m
}

fun calculateTotalDistance(path: List<LatLng>): Double {
    var totalDistance = 0.0
    for (i in 0 until path.size - 1) {
        totalDistance += calculateDistance(
            path[i].latitude, path[i].longitude,
            path[i + 1].latitude, path[i + 1].longitude
        )
    }
    return totalDistance // 단위: m
}
fun bitmapToByteArray(bitmap: Bitmap, format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG, quality: Int = 100): ByteArray {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(format, quality, outputStream)
    return outputStream.toByteArray()
}




@Composable
@Preview(showSystemUi = true)
fun WalkScreenPreview() {
    WalkScreen(rememberNavController())
}