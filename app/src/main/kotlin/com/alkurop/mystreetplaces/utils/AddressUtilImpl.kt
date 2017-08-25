package com.alkurop.mystreetplaces.utils

import android.content.Context
import android.location.Address
import com.google.android.gms.maps.model.LatLng
import android.location.Geocoder
import io.reactivex.Single
import timber.log.Timber
import java.util.*

class AddressUtilImpl(val context: Context) : AddressUtil {

    val geocoder = Geocoder(context, Locale.getDefault())

    override fun getAddress(location: LatLng): Single<MutableList<Address>> {
        return Single.fromCallable {
            geocoder.getFromLocation(location.latitude, location.longitude, 10)
        }.onErrorReturn {
            Timber.e(it)
            listOf()
        }
    }
}
