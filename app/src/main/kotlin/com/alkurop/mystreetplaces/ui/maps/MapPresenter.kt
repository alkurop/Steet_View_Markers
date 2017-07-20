package com.alkurop.mystreetplaces.ui.maps

import android.location.Location
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import io.reactivex.subjects.Subject

interface MapPresenter {
    val viewBus: Subject<MapViewModel>
    val navBus: Subject<NavigationAction>

    fun onGoToStreetView(location: Location)

    fun onAddMarker(location: Location)

    fun unsubscribe()
}

