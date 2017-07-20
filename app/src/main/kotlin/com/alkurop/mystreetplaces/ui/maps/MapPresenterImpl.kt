package com.alkurop.mystreetplaces.ui.maps

import com.alkurop.mystreetplaces.ui.createNavigationSubject
import com.alkurop.mystreetplaces.ui.createViewSubject
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.alkurop.mystreetplaces.utils.LocationTracker
import com.google.android.gms.maps.model.LatLng
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.Subject
import timber.log.Timber
import java.util.concurrent.TimeUnit


class MapPresenterImp : MapPresenter {
    override val viewBus: Subject<MapViewModel> = createViewSubject()
    override val navBus: Subject<NavigationAction> = createNavigationSubject()

    override var isPermissionGranted: Boolean = false
    override lateinit var locationTracker: LocationTracker

    val GET_LAST_KNOWN_LOCATION_TIMEOUT: Long = 1
    val compositeDisposable = CompositeDisposable()

    override fun onAddMarker() {
        if (isPermissionGranted) {
            val disposable = locationTracker
                    .getLastKnownLocation()
                    .firstElement()
                    .timeout(GET_LAST_KNOWN_LOCATION_TIMEOUT, TimeUnit.SECONDS)
                    .subscribe({}, {
                        Timber.e(it)
                    })
            compositeDisposable.add(disposable)
        } else {
            viewBus.onNext(MapViewModel(shouldAskForPermission = true))
        }
    }

    override fun onGoToStreetView(location: LatLng?) {
        if (location == null) {
            if (isPermissionGranted) {
            } else {
                viewBus.onNext(MapViewModel(shouldAskForPermission = true))
            }
        } else {
            val disposable = locationTracker
                    .getLastKnownLocation()
                    .firstElement()
                    .timeout(GET_LAST_KNOWN_LOCATION_TIMEOUT, TimeUnit.SECONDS)
                    .subscribe({}, {
                        Timber.e(it)

                    })
            compositeDisposable.add(disposable)
        }
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }
}