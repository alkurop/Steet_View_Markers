package com.alkurop.mystreetplaces.data.search

import android.app.Activity
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.*
import com.google.android.gms.maps.model.LatLngBounds
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
            it.addOnCompleteListener { publisher.onComplete() }
            it.addOnSuccessListener { publisher.onNext(it.map { it }) }
            it.addOnFailureListener(publisher::onError)
        }
        return publisher.take(1).singleOrError().map { it.map { GooglePlace(it) } }
    }

    override fun getPlacePicturesMetadata(place: GooglePlace): Single<List<PlacePhotoMetadata>> {
        val publisher = PublishSubject.create<List<PlacePhotoMetadata>>()

        geoClient.getPlacePhotos(place.id)
                .addOnSuccessListener {
                    val list = it.photoMetadata.map { it }
                    publisher.onNext(list)
                }
                .addOnCompleteListener { publisher.onComplete() }
                .addOnFailureListener(publisher::onError)

        return publisher.take(1).singleOrError()
    }

    fun getPhoto(client: GoogleApiClient, data: PlacePhotoMetadata) {
        data.getPhoto(client)
                .setResultCallback{
                    if (it.status.isSuccess) {

                    }
                    if (it.status.isCanceled) {

                    }
                    it.status.statusCode
                }
    }
}