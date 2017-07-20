package com.alkurop.mystreetplaces.ui.maps

import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.alkurop.mystreetplaces.utils.LocationTracker
import io.reactivex.subjects.Subject

interface MapPresenter {
    val viewBus: Subject<MapViewModel>
    val navBus: Subject<NavigationAction>
    var locationTracker:LocationTracker
    var isPermissionGranted:Boolean

    fun onGoToStreetView()

    fun onAddMarker()

    fun unsubscribe()
}

