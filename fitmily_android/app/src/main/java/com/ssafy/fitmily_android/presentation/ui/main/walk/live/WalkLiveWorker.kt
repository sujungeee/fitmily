package com.ssafy.fitmily_android.presentation.ui.main.walk.live

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.gson.Gson
import com.ssafy.fitmily_android.model.dto.request.walk.GpsRequest
import com.ssafy.fitmily_android.model.dto.response.walk.GpsDto

private const val TAG = "WalkLiveWorker"
class WalkLiveWorker(private val context: Context) {

    private val locationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    init {
        WalkLiveData.lat = 0.0
        WalkLiveData.lon = 0.0
    }

    fun startLocationUpdates() {
        if(ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED){
            return
        }

        locationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            Log.d(TAG, "startLocationUpdates: ${location.toString()}")
            if (location != null) {
                WalkLiveData.apply{
                    lat = location.latitude
                    lon = location.longitude
                    speed = location.accuracy.toDouble()
                }
            }


        }

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).apply{
            setMinUpdateDistanceMeters(0f)
            setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            setWaitForAccurateLocation(true)
        }.build()

        locationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }
    var pp = 0.001
    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            if (locationResult.locations[0] != null) {
                WalkLiveData.apply{
                    lat = locationResult.locations[0].latitude
                    lon = locationResult.locations[0].longitude
                    lastUpdatedTime = System.currentTimeMillis()
                }
                val data =  GpsRequest(
                    WalkLiveData.userId,
                    WalkLiveData.lat+pp,
                    WalkLiveData.lon,
                    System.currentTimeMillis().toString(),
                )
                pp+= 0.001

                Log.d(TAG, "startLocationUpdates: ${WalkLiveData.gpsList.value}")
                val jsonMessage = Gson().toJson(data)
                try {
                    WebSocketManager.stompClient.send("/app/walk/gps", jsonMessage).subscribe()
                    Log.d(TAG, "startLocationUpdates: 스톰프 send")
                } catch (e: Exception) {
                    Log.d(TAG, "startLocationUpdates: ${e.message }")
                }

            }
        }
    }
    fun removeLocationUpdates() {
        locationProviderClient.removeLocationUpdates(locationCallback)
    }

}