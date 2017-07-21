package com.alkurop.mystreetplaces.ui.street

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.ui.base.BaseMvpFragment
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_street.*
import timber.log.Timber
import javax.inject.Inject


class StreetFragment : BaseMvpFragment<StreetViewModel>() {

    companion object {
        val FOCUS_LOCATION_KEY = "focus_location"

        fun getNewInstance(focusLocation: LatLng): Fragment {
            val args = Bundle()
            args.putParcelable(FOCUS_LOCATION_KEY, focusLocation)
            val fragment = StreetFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject lateinit var presenter: StreetPresenter
    override fun getSubject(): Observable<StreetViewModel> = presenter.viewBus

    override fun getNavigation(): Observable<NavigationAction> = presenter.navBus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_street, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        marker_view.onCreate(savedInstanceState)
        val focusLocation = arguments.getParcelable<LatLng>(FOCUS_LOCATION_KEY)
        marker_view.focusToLocation(focusLocation)
        fab.setOnClickListener { presenter.dropPin() }
        setStreetViewListeners()
    }

    fun setStreetViewListeners() {
        marker_view.onMarkerClickListener = {
            Toast.makeText(activity, "maker was clicked $it", Toast.LENGTH_SHORT).show()
        }
        marker_view.onMarkerLongClickListener = {
            Toast.makeText(activity, "maker was long clicked $it", Toast.LENGTH_SHORT).show()
        }
        marker_view.onStreetLoadedSuccess = { loadedSuccss ->
            if (!loadedSuccss) {
                presenter.errorLoadingStreetView()
                Toast.makeText(activity, "This place cannot be shown in street view. Show user some other view", Toast.LENGTH_SHORT).show()
            }
        }
        marker_view.onCameraUpdateListener = {
            presenter.onCameraUpdate(it)
            Timber.d("camera position changed. new position $it")
        }
    }

    override fun renderView(viewModel: StreetViewModel) {
        with(viewModel) {
            errorRes?.let { Toast.makeText(activity, it, Toast.LENGTH_SHORT).show() }
        }
    }

    override fun onResume() {
        super.onResume()
        marker_view.onResume()
    }

    override fun onPause() {
        super.onPause()
        marker_view.onPause()
    }

    override fun onLowMemory() {
        marker_view.onLowMemory()
        super.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        val markerState = marker_view.onSaveInstanceState(outState)
        super.onSaveInstanceState(markerState)
    }

    override fun onDestroyView() {
        marker_view.onDestroy()
        super.onDestroyView()
    }

    override fun unsubscribe() {
        presenter.unsubscribe()
    }
}