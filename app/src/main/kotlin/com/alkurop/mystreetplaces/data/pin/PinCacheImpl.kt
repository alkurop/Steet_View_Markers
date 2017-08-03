package com.alkurop.mystreetplaces.data.pin

import com.alkurop.mystreetplaces.db.RealmProvider
import com.alkurop.mystreetplaces.domain.pin.PinDto
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Completable
import io.reactivex.Single
import io.realm.RealmList

class PinCacheImpl(val realmProvider: RealmProvider) : PinCahe {
    override fun addOrUpdatePin(pin: PinDto): Single<PinDto> {
        return Single.fromCallable {
            realmProvider.provideRealm().use {
                it.beginTransaction()
                pin.timeStamp = System.currentTimeMillis()
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

    override fun updateLocalPictures(pinDto: PinDto): Completable {
        return Completable.fromAction {
            realmProvider.provideRealm().use {
                it.beginTransaction()
                val result = it.where(PinDto::class.java)
                        .equalTo("id", pinDto.id)
                        .findFirst()
                val realmList = RealmList<PictureWrapper>()
                pinDto.pictures.forEach { item -> realmList.add(it.copyToRealmOrUpdate(item)) }
                result.pictures = realmList

                it.commitTransaction()
            }
        }
    }
}
