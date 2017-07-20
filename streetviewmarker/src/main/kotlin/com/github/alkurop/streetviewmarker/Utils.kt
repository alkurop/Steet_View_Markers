package com.github.alkurop.streetviewmarker

import android.location.Location
import com.google.android.gms.maps.model.LatLng


fun calculatePlaceDistance(myLocation: LatLng, markerLocation: LatLng): Double {
  val results = FloatArray(1)
  Location.distanceBetween(markerLocation.latitude, markerLocation.longitude,
      myLocation.latitude, myLocation.longitude,
      results)
  return results[0].toDouble() / 1000
}
