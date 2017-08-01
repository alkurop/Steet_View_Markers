package com.alkurop.mystreetplaces.data.pin

import com.alkurop.mystreetplaces.db.RealmProvider
import com.alkurop.mystreetplaces.domain.pin.PinDto
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Maybe
import io.reactivex.Single

class PinCacheImpl(val realmProvider: RealmProvider) : PinCahe {
    override fun addOrUpdatePin(pin: PinDto): Single<PinDto> {
        return Single.fromCallable {
            realmProvider.provideRealm().use {
                it.beginTransaction()
                it.insertOrUpdate(pin)
                it.commitTransaction()
                return@fromCallable pin
            }
        }
    }

    override fun removePin(pin: PinDto): Single<PinDto> {
        return Single.fromCallable {
            realmProvider.provideRealm().use {
                it.beginTransaction()
                it.where(PinDto::class.java)
                        .equalTo("id", pin.id)
                        .findAll().deleteAllFromRealm()
                it.commitTransaction()
                return@fromCallable pin
            }
        }
    }

    override fun getPinsByLocationSquare(minMaxPoints: Array<LatLng>): Single<Array<PinDto>> {
        return Single.fromCallable {
            realmProvider.provideRealm().use {
                val result = it.where(PinDto::class.java)
                        .between("lat", minMaxPoints[0].latitude, minMaxPoints[1].latitude)
                        .between("lon", minMaxPoints[0].longitude, minMaxPoints[1].longitude)
                        .findAll()
                it.copyFromRealm(result)
                        .toTypedArray()
            }
        }
    }

    override fun getPinDetails(id: String): Single<PinDto> {
        return Single.fromCallable {
            realmProvider.provideRealm().use {
                val result = it.where(PinDto::class.java)
                        .equalTo("id", id)
                        .findFirst()
                it.copyFromRealm(result)
            }
        }
    }
}
