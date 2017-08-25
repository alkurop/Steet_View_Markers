package com.alkurop.mystreetplaces.utils

import android.location.Address
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single

interface AddressUtil {

    fun getAddress(location: LatLng): Single<MutableList<Address>>

}