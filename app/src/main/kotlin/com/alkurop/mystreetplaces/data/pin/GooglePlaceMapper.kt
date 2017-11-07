package com.alkurop.mystreetplaces.data.pin

import com.alkurop.mystreetplaces.data.search.GooglePlace
import com.alkurop.mystreetplaces.domain.pin.PinDto

fun GooglePlace.mapToPin(): PinDto {
    val descriptionsSb = StringBuilder()
    if (this.attributions.isNullOrEmpty().not()) {
        descriptionsSb.append(attributions)
        descriptionsSb.append("\n")
    }
    if (this.phoneNumber.isNullOrEmpty().not()) {
        descriptionsSb.append(this.phoneNumber)
        descriptionsSb.append("\n")
    }
    if (this.webSiteUri != null) {
        descriptionsSb.append(this.webSiteUri)
        descriptionsSb.append("\n")
    }

    val pinDto = PinDto().apply {
        id = this@mapToPin.id
        title = name
        description = descriptionsSb.toString()
        isFromGoogle = true
        lat = this@mapToPin.latLon.latitude
        lon = this@mapToPin.latLon.longitude
        address = this@mapToPin.address
    }
    return pinDto
}