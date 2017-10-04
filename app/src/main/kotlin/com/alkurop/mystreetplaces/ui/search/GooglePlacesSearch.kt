package com.alkurop.mystreetplaces.ui.search

import com.google.android.gms.location.places.AutocompletePrediction
import com.google.android.gms.location.places.Place
import com.google.android.gms.maps.model.LatLngBounds
import io.reactivex.Single

interface GooglePlacesSearch {
    fun getPlaces(query: String, bounds: LatLngBounds): Single<List<AutocompletePrediction>>
    fun getPlace(prediction: AutocompletePrediction): Single<List<Place>>
}
