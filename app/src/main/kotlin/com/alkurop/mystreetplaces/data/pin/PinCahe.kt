package com.alkurop.mystreetplaces.data.pin

import com.alkurop.mystreetplaces.domain.pin.PinDto
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single

interface PinCahe {
    fun addOrUpdatePin(pin: PinDto): Single<PinDto>

    fun removePin(pin: PinDto): Single<PinDto>

    fun getPinsByLocationSquare(minMaxPoints:Array<LatLng>): Single<Array<PinDto>>
}