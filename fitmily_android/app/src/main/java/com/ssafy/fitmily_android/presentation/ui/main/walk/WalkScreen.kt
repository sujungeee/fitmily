package com.ssafy.fitmily_android.presentation.ui.main.walk

import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationOverlay
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.PathOverlay
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource
import com.naver.maps.map.overlay.OverlayImage
import com.ssafy.fitmily_android.MainApplication
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.model.dto.request.walk.WalkEndRequest
import com.ssafy.fitmily_android.model.dto.response.walk.GpsDto
import com.ssafy.fitmily_android.presentation.ui.main.home.component.ProfileItem
import com.ssafy.fitmily_android.presentation.ui.main.walk.component.StopWalkDialog
import com.ssafy.fitmily_android.presentation.ui.main.walk.live.WalkLiveData
import com.ssafy.fitmily_android.presentation.ui.main.walk.live.WalkMapCaptureHelper
import com.ssafy.fitmily_android.presentation.ui.main.walk.live.WebSocketManager
import com.ssafy.fitmily_android.ui.theme.mainBlue
import com.ssafy.fitmily_android.ui.theme.mainGray
import com.ssafy.fitmily_android.ui.theme.mainWhite
import com.ssafy.fitmily_android.ui.theme.secondaryBlue
import com.ssafy.fitmily_android.util.LocationUtil
import com.ssafy.fitmily_android.util.ProfileUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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


    val otherData by WalkLiveData.otherData.collectAsState()
    val gpsList by WalkLiveData.gpsList.collectAsState()
    val context = LocalContext.current

    val walkUiState by walkViewModel.uiState.collectAsState()

    var isWalking = remember { mutableStateOf(false) }
    var isDialogOpen = remember { mutableStateOf(false) }
    var watching = remember { mutableStateOf(-1) }

    var path = remember { mutableStateOf(listOf<LatLng>()) }
    var bitmapa = remember { mutableStateOf<Bitmap?>(null) }
    val elapsedTime = remember { mutableStateOf(0L) }
    val totalDistance = remember { mutableStateOf(0.0) }
    val coroutineScope = rememberCoroutineScope()

    var walkDistanceSnapshot :Double = 1.0

    var familyId : Int

    val countdown = remember { mutableStateOf(-1) }

    var userId:Int
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(Unit) {
        val observer = Observer<Boolean> { shouldUpdate ->
            if (shouldUpdate == true) {
                coroutineScope.launch {
                    familyId = MainApplication.getInstance().getDataStore().getFamilyId()
                    userId = MainApplication.getInstance().getDataStore().getUserId()
                    walkViewModel.getWalkingMembers(familyId, userId )
                    WalkLiveData.shouldUpdateOtherGps.value = false
                }
            }
        }
        WalkLiveData.shouldUpdateOtherGps.observe(lifecycleOwner, observer)
        onDispose {
            WalkLiveData.shouldUpdateOtherGps.removeObserver(observer)
        }
    }

    LaunchedEffect(WalkLiveData.isServiceRunning) {
        isWalking.value = WalkLiveData.isServiceRunning.value
    }



    LaunchedEffect(gpsList) {
            path.value = gpsList.map {
                LatLng(it.lat, it.lon)
            }
            totalDistance.value = calculateTotalDistance(path.value)
    }

    LaunchedEffect(otherData) {
        if (!isWalking.value && otherData != null) {
            walkViewModel.updateOtherGpsList(otherData!!)
        }
    }

    LaunchedEffect(walkUiState.otherGpsList) {
        Log.d(TAG, "WalkScreen: ${walkUiState.otherGpsList}")
        path.value = walkUiState.otherGpsList.map {
            LatLng(it.lat, it.lon)
        }
    }


    LaunchedEffect(isWalking.value) {
        if (isWalking.value) {
            walkViewModel.deleteWalkingMembers()
            elapsedTime.value = 0L

            while (isWalking.value) {
                elapsedTime.value =
                    ((System.currentTimeMillis() - WalkLiveData.startedTime) / 1000)
                delay(1000L)
            }
        } else {
            familyId= MainApplication.getInstance().getDataStore().getFamilyId()
            walkViewModel.getWalkingMembers(familyId, userId= MainApplication.getInstance().getDataStore().getUserId())
            elapsedTime.value = 0L
        }
    }

    LaunchedEffect(watching.value) {
        if (watching.value != -1 && watching.value < walkUiState.walkingFamilyList.size) {
            val tmp = walkUiState.walkingFamilyList[watching.value]
            walkViewModel.getWalkPath(tmp.userId)
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


    Box(){
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
                Text("${String.format("%.2f km", totalDistance.value)}", style = typography.titleMedium)
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
                if (watching.value != -1 || isWalking.value) {
                    var icon = R.drawable.cow_icon
                    var color = mainBlue
                    if (walkUiState.walkingFamilyList.isNotEmpty() && watching.value != -1) {
                        icon =
                            ProfileUtil().typeToProfile(walkUiState.walkingFamilyList[watching.value].userZodiacName) ?: R.drawable.cow_icon
                        color =
                            ProfileUtil().seqToColor(walkUiState.walkingFamilyList[watching.value].userFamilySequence)
                    }
                    if (path.value.size > 1) {
                        PathOverlay(
                            coords = path.value,
                            color = color,
                            width = 10.dp,
                        )

                        LocationOverlay(
                            position = path.value.last(),
                            icon = OverlayImage.fromResource(icon?: R.drawable.cow_icon),
                            zIndex = 1,
                            iconWidth = 150,
                            iconHeight = 150,
                        )
                    }
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(0.5f),
                horizontalAlignment = Alignment.End
            ) {
                items(walkUiState.walkingFamilyList.size) { index ->
                    val tmp = walkUiState.walkingFamilyList[index]
                    FilterChip(
                        modifier = Modifier
                            .padding(4.dp)
                            .height(26.dp),
                        onClick = {
                            if (!WebSocketManager.isConnected) {
                                WebSocketManager.connectStomp(other = true)
                            }
                            if (watching.value != index) {
                                WebSocketManager.subscribeStomp("/topic/walk/gps/${tmp.userId}")

                                if(watching.value !=-1){
                                WebSocketManager.unsubscribeStomp("/topic/walk/gps/${walkUiState.walkingFamilyList[watching.value].userId}")
                                    Log.d(TAG, "WalkScreen: 구독해제 ${walkUiState.walkingFamilyList[watching.value].userId}")
                                }
                                }
                            watching.value = index
                        },
                        selected = if(index==watching.value) true else false,
                        label = {
                            ProfileItem(typography.bodySmall, tmp.userFamilySequence, tmp.userNickname, tmp.userZodiacName,)
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

                            coroutineScope.launch {
                                for (i in 3 downTo 0) {
                                    countdown.value = i
                                    delay(1000L)
                                }
                                path.value = listOf()

                            }
                            isWalking.value = true
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
        if (countdown.value>0){
        Column(modifier = Modifier
            .fillMaxSize()
            .background(mainWhite),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Text(
                text = "산책을 시작합니다.",
                style = typography.titleLarge,
                color = mainBlue,
            )
            Text(
                text = "${countdown.value}",
                style = typography.displayLarge,
                color = mainBlue,
            )

            }

        }


    }


    if (isDialogOpen.value) {
        StopWalkDialog(
            onDismissRequest = { isDialogOpen.value = false },
            onConfirmation = {

                isDialogOpen.value = false

                walkDistanceSnapshot = totalDistance.value
                Log.d(TAG, "WalkScreen: $walkDistanceSnapshot")
                if (path.value.isNotEmpty()) {
                    Log.d(TAG, "WalkScreenaa: ${path.value}")
                    WalkMapCaptureHelper(
                        context = context,
                        path = path.value,
                        onCaptured = { bitmap ->
                            bitmap?.let {
                                bitmapa.value = it
                                (context as? LifecycleOwner)?.lifecycleScope?.launch {
                                    walkViewModel.postWalk(
                                        WalkEndRequest(
                                            walkDistance = String.format("%.2f", walkDistanceSnapshot).toDouble(),
                                            walkStartTime = Instant.ofEpochMilli(WalkLiveData.startedTime)
                                                .atZone(ZoneId.systemDefault())
                                                .toLocalDateTime().toString(),
                                            walkEndTime = Instant.ofEpochMilli(System.currentTimeMillis())
                                                .atZone(ZoneId.systemDefault())
                                                .toLocalDateTime().toString(),
                                            walkRouteImg = System.currentTimeMillis().toString(),
                                        ),
                                        byteArray = bitmapToByteArray(it)
                                    )
                                }
                            }
                        }
                    ).capture()
                }
                WalkLiveData.stopWalkLiveService(context)

                isWalking.value = !isWalking.value
                // 초기화
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
    return (R * c) / 1000.0
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