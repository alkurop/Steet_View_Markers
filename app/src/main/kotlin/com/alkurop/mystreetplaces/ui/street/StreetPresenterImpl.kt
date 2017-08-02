package com.alkurop.mystreetplaces.ui.street

import android.os.Bundle
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.data.pin.PinPlace
import com.alkurop.mystreetplaces.data.pin.PinRepo
import com.alkurop.mystreetplaces.ui.createNavigationSubject
import com.alkurop.mystreetplaces.ui.createViewSubject
import com.alkurop.mystreetplaces.ui.navigation.ActivityNavigationAction
import com.alkurop.mystreetplaces.ui.navigation.BottomsheetFragmentNavigationAction
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.alkurop.mystreetplaces.ui.pin.activity.DropPinActivity
import com.alkurop.mystreetplaces.ui.pin.drop.DropPinFragment
import com.alkurop.mystreetplaces.ui.pin.view.PinFragment
import com.alkurop.mystreetplaces.ui.pin.view.PinViewStartModel
import com.alkurop.mystreetplaces.utils.LocationUtils
import com.github.alkurop.streetviewmarker.CameraPosition
import com.github.alkurop.streetviewmarker.Place
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.Subject
import timber.log.Timber

class StreetPresenterImpl(val pinRepo: PinRepo) : StreetPresenter {
    companion object {
        val METERS_TO_OFFSET_MARKER = 5
    }

    val subscribeToPinsUpdatesDisposable = CompositeDisposable()
    var cameraPosition: CameraPosition? = null

    override val viewBus: Subject<StreetViewModel> = createViewSubject()
    override val navBus: Subject<NavigationAction> = createNavigationSubject()
    override fun errorLoadingStreetView() {
        val viewModel = StreetViewModel(errorRes = R.string.er_no_street_view_for_location)
        viewBus.onNext(viewModel)
    }

    override fun dropPin() {
        cameraPosition?.let {
            val location = it.location
            val bearing = it.camera.bearing
            val markerLocation = LocationUtils.moveAlongBearing(location, bearing.toDouble(), METERS_TO_OFFSET_MARKER)
            val args = Bundle()
            args.putParcelable(DropPinFragment.LOCATION_KEY, markerLocation)
            val navigationAction = ActivityNavigationAction(DropPinActivity::class.java, args)
            navBus.onNext(navigationAction)
        }
    }

    override fun onCameraUpdate(cameraPosition: CameraPosition) {
        val oldCameraLocation = this.cameraPosition?.location
        this.cameraPosition = cameraPosition
        if (oldCameraLocation == cameraPosition.location) return
        refreshPins(cameraPosition)
    }

    override fun refreshPins() {
        cameraPosition?.let { refreshPins(it) }
    }

    private fun refreshPins(cameraPosition: CameraPosition) {
        subscribeToPinsUpdatesDisposable.clear()
        val sub = pinRepo.observePinsByLocationAndRadius(cameraPosition.location, 500)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    val model = StreetViewModel(places = it.map { PinPlace(it) })
                    viewBus.onNext(model)
                }, { Timber.e(it) })
        subscribeToPinsUpdatesDisposable.add(sub)
    }

    override fun unsubscribe() {
        subscribeToPinsUpdatesDisposable.clear()
    }

    override fun onMarkerClicked(place: Place) {
        val args = Bundle()
        val model = PinViewStartModel(shoudShowStreetNavigation = false, shouldShowMap = true, pinId = place.id)

        args.putParcelable(PinFragment.CONFIG, model)
        val action = BottomsheetFragmentNavigationAction(endpoint = PinFragment::class.java, args = args)
        navBus.onNext(action)
    }
}