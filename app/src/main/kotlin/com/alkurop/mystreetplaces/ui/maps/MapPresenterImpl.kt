package com.alkurop.mystreetplaces.ui.maps

import com.alkurop.mystreetplaces.ui.createNavigationSubject
import com.alkurop.mystreetplaces.ui.createViewSubject
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.alkurop.mystreetplaces.utils.LocationTracker
import io.reactivex.subjects.Subject


class MapPresenterImp : MapPresenter {
    override var isPermissionGranted: Boolean = false
    override lateinit var locationTracker: LocationTracker
    override val viewBus: Subject<MapViewModel> = createViewSubject()

    override val navBus: Subject<NavigationAction> = createNavigationSubject()

    override fun onAddMarker() {

     }

    override fun onGoToStreetView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unsubscribe() {
    }
}