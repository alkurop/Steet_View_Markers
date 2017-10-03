package com.alkurop.mystreetplaces.intercom

import com.alkurop.mystreetplaces.domain.pin.PinDto
import com.google.android.gms.maps.model.VisibleRegion
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class AppDataBus {
    val pinSearch = PublishSubject.create<SearchModel>()
    val mapLocation = BehaviorSubject.create<MapLocationModel>()

    init {
        mapLocation.startWith(MapLocationModel(null))
    }

    data class MapLocationModel(val visibleRegion: VisibleRegion?)

    data class SearchModel(val pinDto: PinDto?, val query: String)
}