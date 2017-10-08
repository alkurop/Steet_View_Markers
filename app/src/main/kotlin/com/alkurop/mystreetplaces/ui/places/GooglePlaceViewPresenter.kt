package com.alkurop.mystreetplaces.ui.places

import com.alkurop.mystreetplaces.data.search.GooglePlace
import com.alkurop.mystreetplaces.data.search.GooglePlacesSearch
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.google.android.gms.common.api.GoogleApiClient
import io.reactivex.subjects.Subject

interface GooglePlaceViewPresenter {
    val viewBus: Subject<GooglePlaceViewModel>
    val navBus: Subject<NavigationAction>
    var googleApiClient: GoogleApiClient
    var googleSearch: GooglePlacesSearch

    fun onStart(googlePlace: GooglePlace)

    fun unsubscribe()

    fun onShare()

    fun onNavigate()

    fun onSave()
}