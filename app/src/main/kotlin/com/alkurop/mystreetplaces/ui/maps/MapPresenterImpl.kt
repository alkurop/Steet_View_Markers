package com.alkurop.mystreetplaces.ui.maps

import android.os.Bundle
import com.alkurop.mystreetplaces.data.pin.PinRepo
import com.alkurop.mystreetplaces.domain.pin.PinDto
import com.alkurop.mystreetplaces.ui.createNavigationSubject
import com.alkurop.mystreetplaces.ui.createViewSubject
import com.alkurop.mystreetplaces.ui.navigation.ActivityNavigationAction
import com.alkurop.mystreetplaces.ui.navigation.BottomsheetFragmentNavigationAction
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.alkurop.mystreetplaces.ui.pin.activity.DropPinActivity
import com.alkurop.mystreetplaces.ui.pin.view.PinFragment
import com.alkurop.mystreetplaces.ui.pin.drop.DropPinFragment
import com.alkurop.mystreetplaces.ui.pin.view.PinViewStartModel
import com.alkurop.mystreetplaces.ui.street.StreetActivity
import com.alkurop.mystreetplaces.ui.street.StreetFragment
import com.alkurop.mystreetplaces.utils.LocationTracker
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.VisibleRegion
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.Subject
import timber.log.Timber
import java.util.concurrent.TimeUnit

class MapPresenterImpl(val pinRepo: PinRepo) : MapPresenter {
    override val viewBus: Subject<MapViewModel> = createViewSubject()

    override val navBus: Subject<NavigationAction> = createNavigationSubject()
    override var isPermissionGranted: Boolean = false

    var visibleRegion: VisibleRegion? = null
    var pinsSet = setOf<PinDto>()

    val markersDisposable = CompositeDisposable()
    override fun onAddMarker() {
        val tmp = visibleRegion
        if (tmp!= null) {
            addMarker(tmp.latLngBounds.center)
        } else {
            viewBus.onNext(MapViewModel(shouldAskForPermission = true))
        }
    }

    override fun onCameraPositionChanged(visibleRegion: VisibleRegion?) {
        this.visibleRegion = visibleRegion
        visibleRegion?.let { getPinsForLocationFromRepo(it) }
    }

    fun getPinsForLocationFromRepo(visibleRegion: VisibleRegion) {
        markersDisposable.clear()
        val sub = pinRepo
                .observePinsByLocationCorners(visibleRegion.latLngBounds.southwest, visibleRegion.latLngBounds.northeast)
                .subscribeOn(Schedulers.io())
                .subscribe({ onPinsReceived(it) }, { Timber.e(it) })
        markersDisposable.add(sub)
    }

    private fun onPinsReceived(pins: Array<PinDto>) {
        val newPins = pins.filter { pinsSet.contains(it).not() }
        val removePins = pinsSet.filter { pins.contains(it).not() }

        if (newPins.isEmpty().not() or removePins.isEmpty().not()) {
            pinsSet += newPins
            pinsSet -= removePins
            val model = MapViewModel(pins = pinsSet.toList())
            viewBus.onNext(model)
        }
    }

    fun addMarker(latLng: LatLng) {
        val args = Bundle()
        args.putParcelable(DropPinFragment.LOCATION_KEY, latLng)
        val navigationAction = ActivityNavigationAction(DropPinActivity::class.java, args)
        navBus.onNext(navigationAction)
    }

    override fun onGoToStreetView() {
        val location = visibleRegion?.latLngBounds?.center
        if (location != null) {
            navigateToStreetView(location)
        }
    }

    fun navigateToStreetView(latLng: LatLng) {
        val args = Bundle()
        args.putParcelable(StreetFragment.FOCUS_LOCATION_KEY, latLng)
        val navigationAction = ActivityNavigationAction(StreetActivity::class.java, args)
        navBus.onNext(navigationAction)
    }

    override fun onPinClick(it: MapClusterItem) {
        val args = Bundle()
        val model = PinViewStartModel(shoudShowStreetNavigation = true, shouldShowMap = false, pinId = it.place.pinId)
        args.putParcelable(PinFragment.PIN_ID_KEY, model)
        val action = BottomsheetFragmentNavigationAction(endpoint = PinFragment::class.java, args = args)
        navBus.onNext(action)
    }

    fun <T> getLoadingStateTransformer(): ObservableTransformer<T, T> {
        return ObservableTransformer {
            it
                    .doOnSubscribe {
                        val viewModel = MapViewModel(isLoading = true)
                        viewBus.onNext(viewModel)
                    }
                    .doOnNext {
                        val viewModel = MapViewModel(isLoading = false)
                        viewBus.onNext(viewModel)
                    }
                    .doOnError {
                        val viewModel = MapViewModel(isLoading = false)
                        viewBus.onNext(viewModel)
                    }
        }
    }

    override fun unsubscribe() {
        markersDisposable.clear()
    }
}