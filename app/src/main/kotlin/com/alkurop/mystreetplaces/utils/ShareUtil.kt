package com.alkurop.mystreetplaces.utils

import android.content.Intent
import com.alkurop.mystreetplaces.domain.pin.PinDto
import com.github.alkurop.streetviewmarker.CameraPosition
import com.github.alkurop.streetviewmarker.StreetMarkerView
import io.reactivex.Observable

interface ShareUtil {

    fun createShareIntentFromPin(pin: PinDto): Intent

    fun createShareIntentFromStreetProjection(markerView: StreetMarkerView, cameraPosition: CameraPosition): Observable<Intent>
}