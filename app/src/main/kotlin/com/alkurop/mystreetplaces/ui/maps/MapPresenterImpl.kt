package com.alkurop.mystreetplaces.ui.maps

import android.location.Location
import com.alkurop.mystreetplaces.ui.createNavigationSubject
import com.alkurop.mystreetplaces.ui.createViewSubject
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import io.reactivex.subjects.Subject


class MapPresenterImp : MapPresenter {
    override val viewBus: Subject<MapViewModel> = createViewSubject()

    override val navBus: Subject<NavigationAction> = createNavigationSubject()

    override fun onAddMarker(location: Location) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    override fun onGoToStreetView(location: Location) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unsubscribe() {
    }
}