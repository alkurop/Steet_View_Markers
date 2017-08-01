package com.alkurop.mystreetplaces.data.pin

import com.alkurop.mystreetplaces.domain.pin.PinDto
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

interface PinRepo {

    fun addOrUpdatePin(pin: PinDto): Single<PinDto>

    fun removePin(pin: PinDto): Single<PinDto>

    fun observePinsByLocationAndRadius(location: LatLng, radiusMeters: Int): Observable<Array<PinDto>>

    fun observePinsByLocationCorners(bottomRight: LatLng, topLeft: LatLng): Observable<Array<PinDto>>

    fun getPinDetails(id: String): Single<PinDto>

    fun updateLocalPictures(pinDto: PinDto):Completable

}