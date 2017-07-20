package com.alkurop.mystreetplaces.ui.maps

import com.alkurop.mystreetplaces.ui.createNavigationSubject
import com.alkurop.mystreetplaces.ui.createViewSubject
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.alkurop.mystreetplaces.utils.LocationTracker
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.Subject


class MapPresenterImp : MapPresenter {
    override val viewBus: Subject<MapViewModel> = createViewSubject()
    override val navBus: Subject<NavigationAction> = createNavigationSubject()

    override var isPermissionGranted: Boolean = false
    override lateinit var locationTracker: LocationTracker

    val disposable = CompositeDisposable()

    override fun onAddMarker() {
        if (isPermissionGranted) {
        } else {
            viewBus.onNext(MapViewModel(shouldAskForPermission = true))
        }
    }

    override fun onGoToStreetView() {
        if (isPermissionGranted) {
        } else {
            viewBus.onNext(MapViewModel(shouldAskForPermission = true))
        }
    }

    override fun unsubscribe() {
        disposable.clear()
    }
}