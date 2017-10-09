package com.alkurop.mystreetplaces.data.search

import android.graphics.Bitmap
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.AutocompletePrediction
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.PlacePhotoMetadata
import com.google.android.gms.maps.model.LatLngBounds
import io.reactivex.Observable
import io.reactivex.Single

interface GooglePlacesSearch {
    fun getPlaces(query: String, bounds: LatLngBounds): Single<List<AutocompletePrediction>>

    fun getPlaceListById(prediction: AutocompletePrediction): Single<List<GooglePlace>>

    fun getPlacePicturesMetadata(place: GooglePlace): Single<List<PlacePhotoMetadata>>

    fun getPhoto(client: GoogleApiClient, data: PlacePhotoMetadata): Single<Bitmap>

    fun getPhotoScaled(client: GoogleApiClient, data: PlacePhotoMetadata, x: Int, y: Int): Single<Bitmap>
}
