package com.github.alkurop.streetviewmarker

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import com.google.android.gms.maps.StreetViewPanoramaView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.StreetViewPanoramaCamera
import java.util.*

/**
 * Created by alkurop on 2/3/17.
 */
class StreetMarkerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr), IStreetOverlayView {

    val markerView: StreetOverlayView

    val streetView: StreetViewPanoramaView
    val touchOverlay: TouchOverlayView
    var onStreetLoadedSuccess: ((Boolean) -> Unit)? = null

    var onCameraUpdateListener: ((CameraPosition) -> Unit)? = null
    var onMarkerClickListener: ((Place) -> Unit)? = null
    var onMarkerLongClickListener: ((Place) -> Unit)? = null
    override var mapsConfig: MapsConfig
        set(value) {
            markerView.mapsConfig = value
        }
        get() = markerView.mapsConfig

    var shouldFocusToMyLocation = true

    var markerDataList = hashSetOf<Place>()
    var cam: StreetViewPanoramaCamera? = null

    override fun onLocationUpdate(location: LatLng) {
        markerView.onLocationUpdate(location)
    }

    override fun onCameraUpdate(cameraPosition: StreetViewPanoramaCamera) {
        markerView.onCameraUpdate(cameraPosition)
    }

    override fun onClick() {
        markerView.onClick()
    }

    override fun onLongClick() {
        markerView.onLongClick()
    }


    override fun setLocalClickListener(onClickListener: ((data: MarkerDrawData) -> Unit)?) {
        markerView.setLocalClickListener(onClickListener)
    }

    private fun sendCameraPosition(position: LatLng) {
        cam?.let { camera ->
            val updatePosition = CameraPosition(LatLng(position.latitude,
                    position.longitude), camera)
            onCameraUpdateListener?.invoke(updatePosition)
        }
    }

    fun onMarkerClicker(geoData: MarkerGeoData) {
        if (geoData.distance >= mapsConfig.markerMinPositionToMoveToMarker / 1000.toDouble()) {
            focusToLocation(geoData.place.location)
        }
        onMarkerClickListener?.invoke(geoData.place)
    }

    fun onMarkerLongClicker(geoData: MarkerGeoData) {
        onMarkerLongClickListener?.invoke(geoData.place)
    }

    //CONTROLS

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return markerView.onTouchEvent(event)
    }

    override fun setLongClickListener(onClickListener: ((MarkerDrawData) -> Unit)?) {
        markerView.setLongClickListener(onClickListener)
    }

    fun focusToLocation(location: LatLng) {
        streetView.getStreetViewPanoramaAsync { panorama ->
            panorama.setPosition(LatLng(location.latitude, location.longitude))
            sendCameraPosition(LatLng(location.latitude, location.longitude))
        }
    }

    override fun addMarkers(markers: HashSet<Place>) {
        val addMarkers = markers.filter { marker ->
            !markerDataList.contains(marker)
        }
        if (addMarkers.isNotEmpty())
            markerView.addMarkers(markers)
        markerDataList.addAll(addMarkers)
    }


    //State callbacks

    fun onCreate(state: Bundle?) {
        val streetBundle = state?.getBundle("streetView")
        streetView.onCreate(streetBundle)
        streetView.getStreetViewPanoramaAsync { panorama ->
            markerView.onCameraUpdate(panorama.panoramaCamera)
            panorama.setOnStreetViewPanoramaCameraChangeListener { cameraPosition ->
                cam = cameraPosition
                markerView.onCameraUpdate(cameraPosition)
            }
            panorama.setOnStreetViewPanoramaChangeListener { cameraPosition ->
                if (cameraPosition !== null && cameraPosition.position !== null) {
                    markerView.onLocationUpdate(cameraPosition.position)
                    sendCameraPosition(cameraPosition.position)
                }
                onStreetLoadedSuccess?.invoke(cameraPosition !== null && cameraPosition.links != null)
            }
            panorama.setOnStreetViewPanoramaClickListener {
                onClick()
            }
            panorama.setOnStreetViewPanoramaLongClickListener {
                onLongClick()
            }

        }
        touchOverlay.onTouchListener = {
            markerView.onTouchEvent(it)
        }

        markerView.setLongClickListener { onMarkerLongClicker(it.matrixData.data) }

        markerView.setLocalClickListener {
            onMarkerClicker(it.matrixData.data)
        }
        markerView.setLongClickListener { onMarkerLongClicker(it.matrixData.data) }
        restoreState(state)
    }

    private fun restoreState(saveState: Bundle?) {
        saveState?.let {
            shouldFocusToMyLocation = saveState.getBoolean("shouldFocusToMyLocation", true)
            @Suppress("UNCHECKED_CAST")
            markerDataList = (saveState.getParcelableArray("markerModels") as Array<Place>).toHashSet()
        }
        markerView.addMarkers(markerDataList)
    }

    fun onSaveInstanceState(state: Bundle?): Bundle {
        val bundle = state ?: Bundle()
        bundle.putParcelableArray("markerModels", markerDataList.toTypedArray())
        bundle.putBoolean("shouldFocusToMyLocation", shouldFocusToMyLocation)
        val streetBundle = Bundle()
        streetView.onSaveInstanceState(streetBundle)
        bundle.putBundle("steetView", streetBundle)
        markerDataList.clear()
        return bundle
    }

    fun onResume() {
        streetView.onResume()
        markerView.postDelayed({ cam?.let { markerView.onCameraUpdate(it) } }, 300)
    }

    fun onPause() {
        streetView.onPause()
    }

    fun onDestroy() {
        streetView.onDestroy()
        markerView.stop()
    }

    fun onLowMemory() = streetView.onLowMemory()

    init {
        inflate(context, R.layout.view_street_marker, this)
        markerView = findViewById(R.id.overlay) as StreetOverlayView
        streetView = findViewById(R.id.panorama) as StreetViewPanoramaView
        touchOverlay = findViewById(R.id.touch) as TouchOverlayView
    }
}
