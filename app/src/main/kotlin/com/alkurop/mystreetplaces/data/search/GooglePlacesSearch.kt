package com.alkurop.mystreetplaces.data.search

import com.google.android.gms.location.places.AutocompletePrediction
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.PlacePhotoMetadata
import com.google.android.gms.maps.model.LatLngBounds
import io.reactivex.Single

interface GooglePlacesSearch {
    fun getPlaces(query: String, bounds: LatLngBounds): Single<List<AutocompletePrediction>>
    fun getPlaceListById(prediction: AutocompletePrediction): Single<List<GooglePlace>>
    fun getPlacePicturesMetadata(place: GooglePlace): Single<List<PlacePhotoMetadata>>
}
