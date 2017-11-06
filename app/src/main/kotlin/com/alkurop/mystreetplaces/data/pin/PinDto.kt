package com.alkurop.mystreetplaces.domain.pin

import com.alkurop.mystreetplaces.data.pin.PictureWrapper
import com.google.android.gms.maps.model.LatLng
import io.realm.RealmList
import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

/**
 * Created by alkurop on 7/21/17.
 */
@RealmClass
open class PinDto : RealmModel {
    @PrimaryKey
    var id: String? = null
    lateinit var title: String
    lateinit var description: String
    var ownerId: String? = null
    var lat: Double = 0.0
    var lon: Double = 0.0
    var pictures = RealmList<PictureWrapper>()
    var isSynchronized: Boolean = false
    var timeStamp: Long = 0
    var address: String? = null
    var categoryId: String? = null
    var isFromGoogle: Boolean = false
    var isTemp: Boolean = false

    constructor()

    constructor(location: LatLng, title: String, description: String = "") {
        this.lat = location.latitude
        this.lon = location.longitude
        this.title = title
        this.description = description
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as PinDto

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int = id?.hashCode() ?: 0

}

