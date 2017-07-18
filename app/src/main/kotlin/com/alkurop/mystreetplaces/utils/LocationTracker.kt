package com.alkurop.mystreetplaces.utils

import android.content.Intent
import android.location.Location
import android.support.v4.app.FragmentActivity


interface LocationListener {
    fun onFailed()
    fun onLocationUpdate(location: Location)
}

interface LocationTracker {
    fun deactivate()
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent)
    fun start(activity: FragmentActivity, listener: LocationListener)
}