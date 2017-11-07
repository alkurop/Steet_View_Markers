package com.alkurop.mystreetplaces.data.pin

import com.alkurop.mystreetplaces.db.RealmProvider
import com.alkurop.mystreetplaces.domain.pin.PinDto
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Completable
import io.reactivex.Single
import io.realm.*

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

    override fun addTempPin(pin: PinDto): Single<PinDto> {
        return Single.fromCallable {
            realmProvider.provideRealm().use {
                it.beginTransaction()
                pin.timeStamp = System.currentTimeMillis()
                it.where(PinDto::class.java).equalTo("isTemp", true)
                        .findAll().deleteAllFromRealm()
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

    override fun deletePicture(id: String): Completable {
        return Completable.fromAction {
            realmProvider.provideRealm().use {
                it.beginTransaction()
                it.where(PictureWrapper::class.java)
                        .equalTo("id", id)
                        .findFirst()
                        ?.deleteFromRealm()
                it.commitTransaction()
            }
        }
    }

    override fun searchSync(query: String): List<PinDto> {
        val wordsSplit = query.split(" ")
        val realm = realmProvider.provideRealm()
        var searchRequest = realm.where(PinDto::class.java)
        wordsSplit.forEach {
            searchRequest = searchRequest.addQuery(it)
        }
        val request = searchRequest
                .findAllSorted("timeStamp", Sort.DESCENDING)
                .take(10)
        return realm.copyFromRealm(request)
    }

    fun <E : RealmModel> RealmQuery<E>.addQuery(query: String): RealmQuery<E> {
        return this
                .beginGroup()
                .contains("title", query, Case.INSENSITIVE)
                .or()
                .contains("description", query, Case.INSENSITIVE)
                .or()
                .contains("address", query, Case.INSENSITIVE)
                .endGroup()
    }
}
