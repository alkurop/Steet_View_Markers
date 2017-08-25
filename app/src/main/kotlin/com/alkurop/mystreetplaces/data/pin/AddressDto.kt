package com.alkurop.mystreetplaces.data.pin

import android.location.Address
import io.realm.RealmModel
import io.realm.annotations.RealmClass

@RealmClass
open class AddressDto : RealmModel {
    var addressLine: String? = null
    var locality: String? = null
    var countryCode: String? = null
    var contruName: String? = null
    var postalCode: String? = null

    constructor()
    constructor(address: Address) {
        addressLine = address.getAddressLine(0)
        locality = address.locality
        contruName = address.countryName
        countryCode = address.countryCode
        postalCode = address.postalCode
    }
}