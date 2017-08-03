package com.alkurop.mystreetplaces.data.pin

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class PictureWrapper : RealmObject {
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

}