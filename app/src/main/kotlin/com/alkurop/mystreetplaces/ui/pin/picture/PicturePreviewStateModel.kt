package com.alkurop.mystreetplaces.ui.pin.picture

import android.os.Parcel
import android.os.Parcelable
import com.alkurop.mystreetplaces.data.pin.PictureWrapper


data class PicturePreviewStateModel(val picturesList: List<PictureWrapper>, val startIndex: Int) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<PicturePreviewStateModel> = object : Parcelable.Creator<PicturePreviewStateModel> {
            override fun createFromParcel(source: Parcel): PicturePreviewStateModel = PicturePreviewStateModel(source)
            override fun newArray(size: Int): Array<PicturePreviewStateModel?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.createTypedArrayList(PictureWrapper.CREATOR),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeTypedList(picturesList)
        dest.writeInt(startIndex)
    }
}