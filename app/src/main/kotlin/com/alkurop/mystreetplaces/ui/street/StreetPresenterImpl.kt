package com.alkurop.mystreetplaces.ui.street

import android.os.Bundle
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.ui.createNavigationSubject
import com.alkurop.mystreetplaces.ui.createViewSubject
import com.alkurop.mystreetplaces.ui.navigation.ActivityNavigationAction
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.alkurop.mystreetplaces.ui.pin.activity.DropPinActivity
import com.alkurop.mystreetplaces.ui.pin.drop.DropPinFragment
import com.alkurop.mystreetplaces.utils.LocationUtils
import com.github.alkurop.streetviewmarker.CameraPosition
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.Subject


class StreetPresenterImpl : StreetPresenter {
    companion object {
        val METERS_TO_OFFSET_MARKER = 5
    }

    val compositeDisposable = CompositeDisposable()
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
        this.cameraPosition = cameraPosition
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }
}