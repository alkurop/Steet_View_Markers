package com.alkurop.mystreetplaces.data.search

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.location.places.Place
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import java.util.*

data class GooglePlace(val id: String,
                       val placeTypes: List<Int>,
                       val address: String?,
                       val locale: Locale?,
                       val name: String,
                       val latLon: LatLng,
                       val viewPort: LatLngBounds?,
                       val webSiteUri: Uri?,
                       val phoneNumber: String?,
                       val rating: Float,
                       val priceLevel: Int,
                       val attributions: String?) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            ArrayList<Int>().apply { source.readList(this, Int::class.java.classLoader) },
            source.readString(),
            source.readSerializable() as Locale,
            source.readString(),
            source.readParcelable<LatLng>(LatLng::class.java.classLoader),
            source.readParcelable<LatLngBounds>(LatLngBounds::class.java.classLoader),
            source.readParcelable<Uri>(Uri::class.java.classLoader),
            source.readString(),
            source.readFloat(),
            source.readInt(),
            source.readString()
    )

    constructor(place: Place) : this(
            place.id,
            place.placeTypes,
            place.address?.toString(),
            place.locale,
            place.name.toString(),
            place.latLng,
            place.viewport,
            place.websiteUri,
            place.phoneNumber?.toString(),
            place.rating,
            place.priceLevel,
            place.attributions?.toString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeList(placeTypes)
        writeString(address)
        writeSerializable(locale)
        writeString(name)
        writeParcelable(latLon, 0)
        writeParcelable(viewPort, 0)
        writeParcelable(webSiteUri, 0)
        writeString(phoneNumber)
        writeFloat(rating)
        writeInt(priceLevel)
        writeString(attributions)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<GooglePlace> = object : Parcelable.Creator<GooglePlace> {
            override fun createFromParcel(source: Parcel): GooglePlace = GooglePlace(source)
            override fun newArray(size: Int): Array<GooglePlace?> = arrayOfNulls(size)
        }
    }
}