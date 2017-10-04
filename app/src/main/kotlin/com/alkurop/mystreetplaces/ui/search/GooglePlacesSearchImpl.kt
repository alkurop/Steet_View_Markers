package com.alkurop.mystreetplaces.ui.search

import android.app.Activity
import com.google.android.gms.location.places.AutocompletePrediction
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLngBounds
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

class GooglePlacesSearchImpl(val activity: Activity) : GooglePlacesSearch {
    val geoClient: GeoDataClient = Places.getGeoDataClient(activity, null)

    override fun getPlaces(query: String, bounds: LatLngBounds): Single<List<AutocompletePrediction>> {
        val publisher = PublishSubject.create<List<AutocompletePrediction>>()
        geoClient.getAutocompletePredictions(query, bounds, null)
                .continueWith {
                    it.addOnSuccessListener { publisher.onNext(it.map { it }) }
                    it.addOnFailureListener(publisher::onError)
                    it.addOnCompleteListener { publisher.onComplete() }
                }
        return publisher.take(1).singleOrError()
    }

    override fun getPlace(prediction: AutocompletePrediction): Single<List<Place>> {
        val publisher = PublishSubject.create<List<Place>>()

        geoClient.getPlaceById(prediction.placeId).continueWith {
            it.addOnCompleteListener { publisher.onComplete() }
            it.addOnSuccessListener { publisher.onNext(it.map { it }) }
            it.addOnFailureListener(publisher::onError)
        }
        return publisher.take(1).singleOrError()
    }
}