package com.alkurop.mystreetplaces.ui.street

import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.github.alkurop.streetviewmarker.CameraPosition
import com.github.alkurop.streetviewmarker.Place
import io.reactivex.subjects.Subject

interface StreetPresenter {
    val viewBus: Subject<StreetViewModel>
    val navBus: Subject<NavigationAction>
    var cameraPosition: CameraPosition?

    fun errorLoadingStreetView()

    fun dropPin()

    fun onCameraUpdate(cameraPosition: CameraPosition)

    fun onMarkerClicked(place: Place)

    fun unsubscribe()
    fun refreshPins()
}