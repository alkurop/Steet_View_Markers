package com.alkurop.mystreetplaces.utils

import com.google.android.gms.maps.model.LatLng

object LocationUtils {
    fun moveAlongBearing(location: LatLng, bearing: Double, dist: Int): LatLng {
        val R = 6371e6
        val lat2 = Math.asin(Math.sin(location.latitude) * Math.cos(dist / R) +
                Math.cos(location.latitude) * Math.sin(dist / R) * Math.cos(bearing))
        val lon2 = location.longitude + Math.atan2(Math.sin(bearing) * Math.sin(dist / R)
                * Math.cos(location.latitude), Math.cos(dist / R) - Math.sin(location.latitude) * Math.sin(lat2))
        return LatLng(lat2, lon2)
    }
}