package com.alkurop.mystreetplaces.ui.search

import com.alkurop.mystreetplaces.data.search.GooglePlace
import com.alkurop.mystreetplaces.data.search.GooglePlacesSearch
import com.alkurop.mystreetplaces.domain.pin.PinDto
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.google.android.gms.location.places.AutocompletePrediction
import com.google.android.gms.maps.model.LatLng
import io.reactivex.subjects.Subject

interface SearchPresenter {
    val viewBus: Subject<SearchViewModel>
    val navBus: Subject<NavigationAction>
    var googlePlacesSearch: GooglePlacesSearch

    fun onSearchQuerySubmit(query: String, location: LatLng)

    fun unsubscribe()

    fun onSearchItemSelected(pinDto: PinDto)

    fun onPlaceClicked(place: GooglePlace)
}