package com.alkurop.mystreetplaces.data.search

import android.accounts.NetworkErrorException
import android.app.Activity
import android.graphics.Bitmap
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.*
import com.google.android.gms.maps.model.LatLngBounds
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

class GooglePlacesSearchImpl(val activity: Activity) : GooglePlacesSearch {
    val geoClient: GeoDataClient = Places.getGeoDataClient(activity, null)

    override fun getPlaces(query: String, bounds: LatLngBounds): Single<List<AutocompletePrediction>> {
        val publisher = PublishSubject.create<List<AutocompletePrediction>>()
        val filter = AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE).build()
        geoClient.getAutocompletePredictions(query, bounds, filter)
                .continueWith {
                    it.addOnSuccessListener { publisher.onNext(it.map { it }) }
                    it.addOnFailureListener(publisher::onError)
                    it.addOnCompleteListener { publisher.onComplete() }
                }
        return publisher.take(1).singleOrError()
    }

    override fun getPlaceListById(prediction: AutocompletePrediction): Single<List<GooglePlace>> {
        val publisher = PublishSubject.create<List<Place>>()

        geoClient.getPlaceById(prediction.placeId).continueWith {
            it.addOnSuccessListener {
                publisher.onNext(it.map { it })
            }
            it.addOnFailureListener {
                publisher.onError(it)
            }
        }
        return publisher.map { it.map { GooglePlace(it) }}.take(1).singleOrError()
    }

    override fun getPlacePicturesMetadata(place: GooglePlace): Single<List<PlacePhotoMetadata>> {
        val publisher = PublishSubject.create<List<PlacePhotoMetadata>>()

        geoClient.getPlacePhotos(place.id)
                .addOnSuccessListener {
                    val list = it.photoMetadata.map { it }
                    publisher.onNext(list)
                    publisher.onComplete()
                }
                .addOnFailureListener(publisher::onError)

        return publisher.take(1).singleOrError()
    }

    override fun getPhoto(client: GoogleApiClient, data: PlacePhotoMetadata): Single<Bitmap> {
        val publisher = PublishSubject.create<Bitmap>()
        data.getPhoto(client)
                .setResultCallback {
                    val status = it.status
                    if (status.isSuccess) {
                        publisher.onNext(it.bitmap)
                        publisher.onComplete()
                    } else {
                        publisher.onError(NetworkErrorException("Request failed $status"))
                    }
                }
        return publisher.take(1).singleOrError()
    }

    override fun getPhotoScaled(client: GoogleApiClient, data: PlacePhotoMetadata, x: Int, y: Int): Single<Bitmap> {
        val publisher = PublishSubject.create<Bitmap>()
        data.getScaledPhoto(client, x, y)
                .setResultCallback {
                    val status = it.status
                    if (status.isSuccess) {
                        publisher.onNext(it.bitmap)
                        publisher.onComplete()
                    } else {
                        publisher.onError(NetworkErrorException("Request failed $status"))
                    }
                }
        return publisher.take(1).singleOrError()
    }
}