package com.alkurop.mystreetplaces.data.pin

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.domain.pin.PinDto
import com.github.alkurop.streetviewmarker.Place
import com.google.android.gms.maps.model.LatLng

data class PinPlace(val pinId: String,
                    val pinLocation: LatLng,
                    val title:String,
                    val description:String? = null) : Place, Parcelable {
    
    constructor(pinDto: PinDto) : this(pinDto.id!!, LatLng(pinDto.lat, pinDto.lon), pinDto.title, pinDto.description)

    override fun getId(): String {
        return pinId
    }

    override fun getLocation(): LatLng {
        return pinLocation
    }

    override fun getMarkerPath(): String? {
        return null
    }

    override fun getDrawableRes(): Int {
        return R.mipmap.ic_launcher_round
    }

    override fun getBitmap(): Bitmap? {
        return null
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<PinPlace> = object : Parcelable.Creator<PinPlace> {
            override fun createFromParcel(source: Parcel): PinPlace = PinPlace(source)
            override fun newArray(size: Int): Array<PinPlace?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readString(),
            source.readParcelable<LatLng>(LatLng::class.java.classLoader),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(pinId)
        dest.writeParcelable(pinLocation, 0)
        dest.writeString(title)
        dest.writeString(description)
    }
}