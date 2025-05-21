package com.ssafy.fitmily_android.presentation.ui.main.walk.live

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.ViewGroup
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.PathOverlay

private const val TAG = "WalkMapCaptureHelper"
class WalkMapCaptureHelper(
    private val context: Context,
    private val path: List<LatLng>,
    private val onCaptured: (Bitmap?) -> Unit
) : OnMapReadyCallback {

    private lateinit var mapView: MapView

    fun capture() {
        mapView = MapView(context)
        mapView.onCreate(null)
        mapView.onResume()

        val rootView = (context as? Activity)?.window?.decorView as? ViewGroup
        rootView?.addView(mapView, ViewGroup.LayoutParams(1000, 1000)) // 원하는 사이즈

        mapView.getMapAsync(this)
    }

    override fun onMapReady(naverMap: NaverMap) {
        val pathOverlay = PathOverlay().apply {
            coords = path
            color = 0xFF3498DB.toInt()
            width = 10
        }

        pathOverlay.map = naverMap

        if (path.isNotEmpty()) {
            val center = path[path.size / 2]
            naverMap.cameraPosition = CameraPosition(center, 15.0)
        }

        mapView.postDelayed({
            naverMap.takeSnapshot { bitmap ->
                onCaptured(bitmap)

                val rootView = (context as? Activity)?.window?.decorView as? ViewGroup
                rootView?.removeView(mapView)
                mapView.onDestroy()
            }
        }, 1000L)
    }
}
