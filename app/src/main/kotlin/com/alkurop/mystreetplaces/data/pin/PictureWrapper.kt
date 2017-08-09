package com.alkurop.mystreetplaces.data.pin

import android.os.Parcel
import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class PictureWrapper : RealmObject, Parcelable {
    @PrimaryKey
    lateinit var id: String

    var timeStamp: Long = 0

    var localPhoto: String? = null

    var serverPhoto: String? = null

    constructor()

    constructor(photo: String) {
        this.localPhoto = photo
        this.id = UUID.randomUUID().toString()
        this.timeStamp = System.currentTimeMillis()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as PictureWrapper

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<PictureWrapper> = object : Parcelable.Creator<PictureWrapper> {
            override fun createFromParcel(source: Parcel): PictureWrapper = PictureWrapper(source)
            override fun newArray(size: Int): Array<PictureWrapper?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {}
}