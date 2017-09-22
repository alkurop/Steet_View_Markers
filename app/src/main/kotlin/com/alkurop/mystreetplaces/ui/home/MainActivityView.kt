package com.alkurop.mystreetplaces.ui.home

import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.StringRes
import com.alkurop.mystreetplaces.intercom.SearchBus

data class MainActivityView(@StringRes val toolbarTitleRes: Int = -1, val shouldShowSearch: Boolean = false, val search: SearchBus.SearchModel? = null) : Parcelable {
    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<MainActivityView> = object : Parcelable.Creator<MainActivityView> {
            override fun createFromParcel(source: Parcel): MainActivityView = MainActivityView(source)
            override fun newArray(size: Int): Array<MainActivityView?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readInt(),
            source.readInt() != 0
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(toolbarTitleRes)
        dest.writeInt(if (shouldShowSearch) {
            1
        } else 0)
    }
}