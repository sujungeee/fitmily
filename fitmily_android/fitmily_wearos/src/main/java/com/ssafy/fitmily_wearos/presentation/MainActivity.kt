/*
 * Copyright 2023 Samsung Electronics Co., Ltd. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ssafy.fitmily_wearos.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.Text
import com.ssafy.fitmily_wearos.R
import com.ssafy.fitmily_wearos.presentation.ui.MainScreen
import com.ssafy.fitmily_wearos.presentation.ui.Permission
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var heartRateCountSensor: Sensor? = null


    @SuppressLint("VisibleForTests")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        heartRateCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
        Log.d(TAG, "onCreate: 센서 매니저 : $sensorManager")
        setContent {

            if(heartRateCountSensor == null) {
                Toast.makeText(this, "No Step Detect Sensor!!", Toast.LENGTH_SHORT).show()
            }else{
                Text(
                    text = "Step Detect Sensor Found!!",
                    fontSize = 20.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxSize()
                )
                Toast.makeText(this, "시작해용!!", Toast.LENGTH_SHORT).show()
                if((ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED) ||
                    (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.BODY_SENSORS) == PackageManager.PERMISSION_DENIED)){
                    Toast.makeText(this, "No Permission!!", Toast.LENGTH_SHORT).show()

                    //ask for permission
                    requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACTIVITY_RECOGNITION, Manifest.permission.BODY_SENSORS))

                }else{
                    Log.d(TAG, "onCreate: 권한 get!")
                    //권한있는 경우
                }
            }


//
//            val trackingState by viewModel.trackingState.collectAsStateWithLifecycle()
//            val connectionState by viewModel.connectionState.collectAsStateWithLifecycle()
//            if (trackingState.trackingRunning) {
//                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
//            } else {
//                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
//            }
//            LaunchedEffect(Unit) {
//                viewModel
//                    .messageSentToast
//                    .collect { message ->
//                        Toast.makeText(
//                            applicationContext,
//                            if (message) R.string.sending_success else R.string.sending_failed,
//                            Toast.LENGTH_SHORT,
//                        ).show()
//                    }
//            }
//            Log.i(
//                TAG, "connected: ${connectionState.connected}, " +
//                        "message: ${connectionState.message}, " +
//                        "connectionException: ${connectionState.connectionException}"
//            )
//            connectionState.connectionException?.resolve(this)
//            Permission {
//                MainScreen(connectionState.connected,
//                    connectionState.message,
//                    trackingState.trackingRunning,
//                    trackingState.trackingError,
//                    trackingState.message,
//                    trackingState.valueHR,
//                    trackingState.valueIBI,
//                    { viewModel.startTracking(); Log.i(TAG, "startTracking()") },
//                    { viewModel.stopTracking(); Log.i(TAG, "stopTracking()") },
//                    { viewModel.sendMessage(); Log.i(TAG, "sendMessage()") })
//            }
        }
    }
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()){
        Log.d(TAG, "requestPermissionLauncher: 건수 : ${it.size}")

        var results = true
        it.values.forEach{
            if(it == false) {
                results = false
                return@forEach
            }
        }

        if(!results){
            Toast.makeText(this, "권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }else{
            //모두 권한이 있을 경우
        }
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        Log.d(TAG, "onSensorChanged: ${p0?.sensor?.type}")
        if(p0?.sensor?.type == Sensor.TYPE_HEART_RATE) {
            Log.d(TAG, "p0.values[0]: ${p0.values[0]}")
            Log.d(TAG, "onSensorChanged: ${p0.values.forEach { 
                Log.d(TAG, "onSensorChanged: $it")
            }}")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        TODO("Not yet implemented")
    }

    override fun onResume() {
        super.onResume()
        var bool = sensorManager.registerListener(this, heartRateCountSensor, SensorManager.SENSOR_DELAY_NORMAL)
        if(!bool) {
            Log.d(TAG, "onResume: 센서 등록 실패")
    }
        else {
            Log.d(TAG, "onResume: 센서 등록 성공")
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}
//
//    override fun onResume() {
//        super.onResume()
//        if (!viewModel.connectionState.value.connected) {
//            viewModel.setUpTracking()
//        }
//    }
