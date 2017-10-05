package com.alkurop.mystreetplaces.utils

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

object LocationUtils {
    fun moveAlongBearing(location: LatLng, bearing: Double, distanceMeters: Int): LatLng {
        val R = 6378140.0
        val d = (distanceMeters).toDouble()
        val lat1 = Math.toRadians(location.latitude)
        val lon1 = Math.toRadians(location.longitude)
        val brng = Math.toRadians(bearing)

        val lat2 = Math.asin(Math.sin(lat1) * Math.cos(d / R) +
                Math.cos(lat1) * Math.sin(d / R) * Math.cos(brng))

        val lon2 = lon1 + Math.atan2(Math.sin(brng) * Math.sin(d / R) * Math.cos(lat1),
                Math.cos(d / R) - Math.sin(lat1) * Math.sin(lat2))

        return LatLng(Math.toDegrees(lat2), Math.toDegrees(lon2))
    }

    fun getSquareOfDistanceMeters(location: LatLng, distanceMeters: Int): Array<LatLng> {
        val topBearing = 0.0
        val bottomBearing = 180.0
        val leftBearing = 90.0
        val rightBearing = 270.0

        val halfDistance = distanceMeters / 2

        val topPoint = moveAlongBearing(location, topBearing, halfDistance)
        val bottomPoint = moveAlongBearing(location, bottomBearing, halfDistance)
        val leftPoint = moveAlongBearing(location, leftBearing, halfDistance)
        val rightPoint = moveAlongBearing(location, rightBearing, halfDistance)

        val minPoint = LatLng(bottomPoint.latitude, rightPoint.longitude)
        val maxPoint = LatLng(topPoint.latitude, leftPoint.longitude)

        return arrayOf(minPoint, maxPoint)
    }

    fun getBounds(location: LatLng, distanceMeters: Int): LatLngBounds {
        val squareOfDistanceMeters = getSquareOfDistanceMeters(location, distanceMeters)
        return LatLngBounds(squareOfDistanceMeters[0], squareOfDistanceMeters[1])
    }
}