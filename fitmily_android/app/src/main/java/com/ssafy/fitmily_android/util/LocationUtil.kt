package com.ssafy.fitmily_android.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationUtil {

    fun getLastLocation(
        context: Context,
        onLocationReceived: (lat: Double, lon: Double) -> Unit,
        onFailure: (() -> Unit)? = null
    ) {

        // 위치 권한 체크
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            onFailure?.invoke()
            return
        }

        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if(location != null) {
                    onLocationReceived(location.latitude, location.longitude)
                }
                else {
                    onFailure?.invoke()
                }
            }
            .addOnFailureListener {
                onFailure?.invoke()
            }
    }
}