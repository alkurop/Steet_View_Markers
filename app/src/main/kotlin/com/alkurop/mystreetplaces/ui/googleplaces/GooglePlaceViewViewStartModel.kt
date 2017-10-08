package com.alkurop.mystreetplaces.ui.googleplaces

import android.os.Parcel
import android.os.Parcelable
import com.alkurop.mystreetplaces.data.search.GooglePlace

data class GooglePlaceViewViewStartModel(val shoudShowStreetNavigation: Boolean = false,
                                         val shouldShowMap: Boolean = false,
                                         val place: GooglePlace) : Parcelable {
    constructor(source: Parcel) : this(
            1 == source.readInt(),
            1 == source.readInt(),
            source.readParcelable<GooglePlace>(GooglePlace::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt((if (shoudShowStreetNavigation) 1 else 0))
        writeInt((if (shouldShowMap) 1 else 0))
        writeParcelable(place, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<GooglePlaceViewViewStartModel> = object : Parcelable.Creator<GooglePlaceViewViewStartModel> {
            override fun createFromParcel(source: Parcel): GooglePlaceViewViewStartModel = GooglePlaceViewViewStartModel(source)
            override fun newArray(size: Int): Array<GooglePlaceViewViewStartModel?> = arrayOfNulls(size)
        }
    }
}