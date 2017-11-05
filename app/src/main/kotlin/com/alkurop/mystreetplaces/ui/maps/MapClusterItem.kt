package com.alkurop.mystreetplaces.ui.maps

import com.alkurop.mystreetplaces.data.pin.PinPlace
import com.github.alkurop.streetviewmarker.Place
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class MapClusterItem(val place: PinPlace) : ClusterItem {
    override fun getSnippet(): String? {
        return place.description
    }

    override fun getTitle(): String {
        return place.title
    }

    override fun getPosition(): LatLng? {
        try {
            return LatLng(place.location.latitude, place.location.longitude)
        } catch(e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MapClusterItem

        if (place != other.place) return false

        return true
    }

    override fun hashCode(): Int {
        return place.hashCode()
    }


}