package com.github.alkurop.streetviewmarker

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import com.google.android.gms.maps.StreetViewPanorama
import com.google.android.gms.maps.StreetViewPanoramaView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.StreetViewPanoramaCamera
import java.util.HashSet

class StreetMarkerView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr),
    IStreetOverlayView {

    internal val markerView: StreetOverlayView
    internal val streetView: StreetViewPanoramaView
    internal val touchOverlay: TouchOverlayView

    var onStreetLoadedSuccess: ((Boolean) -> Unit)? = null
    var onCameraUpdateListener: ((CameraPosition) -> Unit)? = null
    var onMarkerClickListener: ((Place) -> Unit)? = null
    var onMarkerLongClickListener: ((Place) -> Unit)? = null
    override var mapsConfig: MapsConfig
        set(value) {
            markerView.mapsConfig = value
        }
        get() = markerView.mapsConfig

    internal var markerDataList = hashSetOf<Place>()
    internal var cam: StreetViewPanoramaCamera = StreetViewPanoramaCamera(0f, 0f, 0f)
    internal var position: LatLng? = null
    lateinit var callback: (StreetViewPanorama) -> Unit

    init {
        inflate(context, R.layout.vsm_view_street_marker, this)
        markerView = findViewById(R.id.overlay)
        streetView = findViewById(R.id.panorama)
        touchOverlay = findViewById(R.id.touch)
    }

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

    private fun sendCameraPosition() {
        val tempPosition = position
        if (tempPosition != null) {
            val updatePosition = CameraPosition(
                LatLng(
                    tempPosition.latitude,
                    tempPosition.longitude
                ), cam
            )
            onCameraUpdateListener?.invoke(updatePosition)
        }
    }

    fun onMarkerClicker(geoData: MarkerGeoData) {
        if (geoData.distance >= mapsConfig.markerMinPositionToMoveToMarker / 1000.toDouble()
            && mapsConfig.navigateToLocationOnMarkerClick) {
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
        markerView?.setLongClickListener(onClickListener)
    }

    fun focusToLocation(location: LatLng) {
        streetView.getStreetViewPanoramaAsync { panorama ->
            panorama.setPosition(LatLng(location.latitude, location.longitude))
            position = location
            sendCameraPosition()
        }
    }

    override fun setMarkers(markers: HashSet<Place>) {
        markerView.setMarkers(markers)
        markerDataList.clear()
        markerDataList.addAll(markers)
    }

    //State callbacks

    fun onCreate(state: Bundle?) {
        val streetBundle = state?.getBundle("streetView")
        streetView.onCreate(streetBundle)
        callback = { panorama ->
            markerView.onCameraUpdate(panorama.panoramaCamera)
            panorama.setOnStreetViewPanoramaCameraChangeListener { cameraPosition ->
                cam = cameraPosition
                markerView.onCameraUpdate(cameraPosition)
                sendCameraPosition()
            }
            panorama.setOnStreetViewPanoramaChangeListener { cameraPosition ->
                if (cameraPosition !== null && cameraPosition.position !== null) {
                    markerView.onLocationUpdate(cameraPosition.position)
                    position = cameraPosition.position
                }
                sendCameraPosition()
                onStreetLoadedSuccess?.invoke(cameraPosition !== null && cameraPosition.links != null)
            }
            panorama.setOnStreetViewPanoramaClickListener {
                onClick()
            }
            panorama.setOnStreetViewPanoramaLongClickListener {
                onLongClick()
            }
        }
        streetView.getStreetViewPanoramaAsync(callback)
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
            val parcelableArray = saveState.getParcelableArray("markerModels")
            val map = parcelableArray.map { it as Place }
            markerDataList = (map).toHashSet()
        }
        markerView.setMarkers(markerDataList)
    }

    fun onSaveInstanceState(state: Bundle?): Bundle {
        val bundle = state ?: Bundle()
        val arrayOfPlaces = markerDataList.toTypedArray()
        bundle.putParcelableArray("markerModels", arrayOfPlaces)
        return bundle
    }

    fun onResume() {
        streetView.onResume()
        markerView.postDelayed({ markerView.onCameraUpdate(cam) }, 300)
    }

    fun onPause() {
        markerView.pause()
        streetView.onPause()
    }

    fun onStart() {
    }

    fun onStop() {
        markerView.stop()
    }

    fun onDestroy() {
        markerView.stop()
        streetView.onDestroy()
    }

    fun onLowMemory() = streetView.onLowMemory()

}
