package com.alkurop.mystreetplaces.ui.pin.picture.container

import android.os.Parcel
import android.os.Parcelable
import com.alkurop.mystreetplaces.data.pin.PictureWrapper


data class PicturePreviewContainerStateModel(val picturesList: List<PictureWrapper>, val startIndex: Int) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<PicturePreviewContainerStateModel> = object : Parcelable.Creator<PicturePreviewContainerStateModel> {
            override fun createFromParcel(source: Parcel): PicturePreviewContainerStateModel = PicturePreviewContainerStateModel(source)
            override fun newArray(size: Int): Array<PicturePreviewContainerStateModel?> = arrayOfNulls(size)
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