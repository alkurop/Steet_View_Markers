package com.alkurop.mystreetplaces.ui.maps

import android.os.Bundle
import com.alkurop.mystreetplaces.data.pin.PinRepo
import com.alkurop.mystreetplaces.data.search.GooglePlace
import com.alkurop.mystreetplaces.domain.pin.PinDto
import com.alkurop.mystreetplaces.intercom.AppDataBus
import com.alkurop.mystreetplaces.ui.createNavigationSubject
import com.alkurop.mystreetplaces.ui.createViewSubject
import com.alkurop.mystreetplaces.ui.navigation.ActivityNavigationAction
import com.alkurop.mystreetplaces.ui.navigation.BottomsheetFragmentNavigationAction
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.alkurop.mystreetplaces.ui.pin.drop.DropPinActivity
import com.alkurop.mystreetplaces.ui.pin.drop.DropPinFragment
import com.alkurop.mystreetplaces.ui.pin.placedetails.PlaceDetailsFragment
import com.alkurop.mystreetplaces.ui.pin.view.PinFragment
import com.alkurop.mystreetplaces.ui.pin.view.PinViewStartModel
import com.alkurop.mystreetplaces.ui.googleplaces.GooglePlaceViewViewStartModel
import com.alkurop.mystreetplaces.ui.street.StreetActivity
import com.alkurop.mystreetplaces.ui.street.StreetFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.VisibleRegion
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import timber.log.Timber
import java.util.concurrent.TimeUnit

class MapPresenterImpl(val pinRepo: PinRepo, val appDataBus: AppDataBus) : MapPresenter {
    override val viewBus: Subject<MapViewModel> = createViewSubject()

    override val navBus: Subject<NavigationAction> = createNavigationSubject()
    override var isPermissionGranted: Boolean = false

    var visibleRegion: VisibleRegion? = null
    val markersDisposable = CompositeDisposable()
    val generalPurposeDisposable = CompositeDisposable()

    val locationChangedPublisher = PublishSubject.create<VisibleRegion>()

    override fun onAddMarker(latLng: LatLng?) {
        val tmp = visibleRegion
        when {
            latLng != null -> addMarker(latLng)
            tmp != null -> addMarker(tmp.latLngBounds.center)
            else -> viewBus.onNext(MapViewModel(shouldAskForPermission = true))
        }
    }

    override fun attach() {
        val sub = appDataBus.pinSearch.
                subscribe({ navigateToItem(it.pinDto?.id!!) })
        val sub2 = appDataBus.googlePlacesSearch.subscribe({
            loadPlace(it)
        })
        generalPurposeDisposable.addAll(sub, sub2)
        val sub3 = locationChangedPublisher.debounce (250, TimeUnit.MILLISECONDS)
                .subscribe({
                    getPinsForLocationFromRepo(it)
                })
        generalPurposeDisposable += sub
        generalPurposeDisposable += sub2
        generalPurposeDisposable += sub3

    }

    fun loadPlace(searchModel: AppDataBus.GooglePlaceSearchModel) {
        focusViewToPlace(searchModel.place)
    }

    override fun onCameraPositionChanged(visibleRegion: VisibleRegion?) {
        this.visibleRegion = visibleRegion

        visibleRegion?.let {
            locationChangedPublisher.onNext(it)
            appDataBus.mapLocation.onNext(AppDataBus.MapLocationModel(it))
        }
    }

    override fun refresh() {
        onCameraPositionChanged(visibleRegion)
    }

    fun getPinsForLocationFromRepo(visibleRegion: VisibleRegion) {
        markersDisposable.clear()
        val sub = pinRepo
                .observePinsByLocationCorners(visibleRegion.latLngBounds.southwest, visibleRegion.latLngBounds.northeast)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    val model = MapViewModel(pins = it.toList())
                    viewBus.onNext(model)
                }, { Timber.e(it) })
        markersDisposable.add(sub)
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
        showMarkerDetails(it.place.pinId)
    }

    override fun unsubscribe() {
        markersDisposable.clear()
        generalPurposeDisposable.clear()
    }

    fun navigateToItem(itemId: String) {
        val subscribe = pinRepo.getPinDetails(itemId).subscribe({
            focusViewToMarker(it)
        }, { Timber.e(it) })
        markersDisposable.add(subscribe)
    }

    fun focusViewToMarker(it: PinDto) {
        val model = MapViewModel(focusMarker = it)
        viewBus.onNext(model)
        showMarkerDetails(it.id!!)
    }

    fun focusViewToPlace(it: GooglePlace) {
        val model = MapViewModel(focusPlace = it)
        viewBus.onNext(model)
        showPlaceDetails(it)
    }

    fun showMarkerDetails(markerId: String) {
        val args = Bundle()
        val model = PinViewStartModel(shoudShowStreetNavigation = true, shouldShowMap = false, pinId = markerId)
        args.putParcelable(PinFragment.CONFIG, model)
        val action = BottomsheetFragmentNavigationAction(endpoint = PinFragment::class.java, args = args)
        navBus.onNext(action)
    }

    fun showPlaceDetails(googlePlace: GooglePlace) {
        val args = Bundle()
        val model = GooglePlaceViewViewStartModel(shoudShowStreetNavigation = true, shouldShowMap = false, place = googlePlace)
        args.putParcelable(PlaceDetailsFragment.KEY_PLACE, model)
        val action = BottomsheetFragmentNavigationAction(endpoint = PlaceDetailsFragment::class.java, args = args)
        navBus.onNext(action)
    }
}
