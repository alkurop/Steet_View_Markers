package com.alkurop.mystreetplaces.utils

import com.google.android.gms.maps.model.LatLng
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class LocationUtilsTest {
    @Test fun testMoveAlongBearing() {
        val location = LatLng(53.203, 54.003)
        val bearing = 123
        val distanceMeters = 50

        val result = LocationUtils.moveAlongBearing(location, bearing.toDouble(), distanceMeters)
        val expectedResult = LatLng(53.2027553696749, 54.0036288889717)
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test fun testLocationSquare() {
        val location = LatLng(53.203, 54.003)
        val distanceMeters = 50

        val result = LocationUtils.getSquareOfDistanceMeters(location, distanceMeters)
        println(result)
        assertThat(result[0].latitude).isLessThan(result[1].latitude)
        assertThat(result[0].longitude).isLessThan(result[1].longitude)

    }
}