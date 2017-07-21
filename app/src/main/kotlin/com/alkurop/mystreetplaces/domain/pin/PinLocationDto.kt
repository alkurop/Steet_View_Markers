package com.alkurop.mystreetplaces.domain.pin

import io.realm.RealmModel
import io.realm.annotations.RealmClass

/**
 * Created by alkurop on 7/21/17.
 */

@RealmClass
open class PinLocationDto : RealmModel {
    var lat: Double = 0.0
    var lon: Double = 0.0

    constructor()
    constructor(lat: Double, lon: Double) {
        this.lat = lat
        this.lon = lon
    }
}