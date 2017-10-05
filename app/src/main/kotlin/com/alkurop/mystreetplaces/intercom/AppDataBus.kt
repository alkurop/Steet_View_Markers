package com.alkurop.mystreetplaces.intercom

import com.alkurop.mystreetplaces.domain.pin.PinDto
import com.google.android.gms.maps.model.VisibleRegion
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class AppDataBus {
    val pinSearch = PublishSubject.create<PinSearchModel>()
    val mapLocation = BehaviorSubject.create<MapLocationModel>()
    val googlePlacesSearch = PublishSubject.create<GooglePlaceSearchModel>()

    init {
        mapLocation.startWith(MapLocationModel(null))
    }

    data class MapLocationModel(val visibleRegion: VisibleRegion?)

    data class PinSearchModel(val pinDto: PinDto?, val query: String)

    data class GooglePlaceSearchModel(val placeId: String, val query: String)

}