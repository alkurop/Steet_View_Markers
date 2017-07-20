package com.alkurop.mystreetplaces.utils

import android.location.Location
import com.google.android.gms.maps.LocationSource

interface MapLocationSource : LocationSource{
    fun getLastKnownLocation(listener: (Location) -> Unit)

}