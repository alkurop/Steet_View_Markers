package com.alkurop.mystreetplaces.domain.pin

import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

/**
 * Created by alkurop on 7/21/17.
 */
@RealmClass
open class PinDto : RealmModel {
    @PrimaryKey lateinit var id: String

    lateinit var title: String

    lateinit var description: String

    lateinit var location: PinLocationDto

    constructor()

    constructor(id: String, location: PinLocationDto, title: String, description: String = "") {
        this.id = id
        this.location = location
        this.title = title
        this.description = description
    }

}