package com.alkurop.mystreetplaces.utils

import android.app.Activity
import android.content.Intent
import android.support.v4.app.FragmentActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.LocationSource
import java.lang.ref.WeakReference


class LocationTrackerImpl : LocationTracker {

    lateinit var apiClient: FusedLocationProviderClient
    var weakActivity = WeakReference<FragmentActivity>(null)
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback
    lateinit var onFailedListener: () -> Unit

    var isTracking = false
    val REQUEST_CHECK_SETTINGS = 220

    override fun setUp(activity: FragmentActivity, onFailedListener: () -> Unit) {
        weakActivity = WeakReference(activity)
        this.onFailedListener = onFailedListener
        apiClient = LocationServices.getFusedLocationProviderClient(activity)
        createLocationRequest()
    }

    override fun activate(listener: LocationSource.OnLocationChangedListener) {
        createLocationCallback(listener)
        executeSettingsRequest()
    }


    fun createLocationCallback(listener: LocationSource.OnLocationChangedListener) {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                listener.onLocationChanged(result.lastLocation)
            }
        }
    }

    fun onRequestFailed() {
        onFailedListener.invoke()
    }

    fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    fun executeSettingsRequest() {
        val activity = weakActivity.get() ?: return
        val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(activity)
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener { executeLocationsRequest() }
        task.addOnFailureListener {
            val code = (it as ApiException).statusCode
            when (code) {
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    val resolvable = it as ResolvableApiException

                    resolvable.startResolutionForResult(
                            activity,
                            REQUEST_CHECK_SETTINGS)
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    onRequestFailed()
                }

            }
        }
    }

    fun executeLocationsRequest() {
        isTracking = true
        apiClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                executeSettingsRequest()
            } else {
                onRequestFailed()
            }
        }
    }

    override fun deactivate() {
        if (isTracking)
            apiClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }
}