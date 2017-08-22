package com.alkurop.mystreetplaces.ui.street

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.Toast
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.ui.base.BaseMvpFragment
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.alkurop.mystreetplaces.utils.ShareUtil
import com.github.alkurop.jpermissionmanager.PermissionsManager
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_street.*
import timber.log.Timber
import javax.inject.Inject

class StreetFragment : BaseMvpFragment<StreetViewModel>() {

    companion object {
        val APP_PACKAGE = "com.alkurop.mystreetplaces.fileprovider"
        val FOCUS_LOCATION_KEY = "focus_location"

        fun getNewInstance(focusLocation: LatLng): Fragment {
            val args = Bundle()
            args.putParcelable(FOCUS_LOCATION_KEY, focusLocation)
            val fragment = StreetFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject lateinit var shareUtil: ShareUtil

    var progressDialog: ProgressDialog? = null
    val compositeDisposable = CompositeDisposable()

    @Inject lateinit var presenter: StreetPresenter
    override fun getSubject(): Observable<StreetViewModel> = presenter.viewBus

    override fun getNavigation(): Observable<NavigationAction> = presenter.navBus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component().inject(this)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_street, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        marker_view.onCreate(savedInstanceState)
        val focusLocation = arguments.getParcelable<LatLng>(FOCUS_LOCATION_KEY)

        marker_view.focusToLocation(focusLocation)
        setStreetViewListeners()
        fab.setOnClickListener { presenter.dropPin() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.street_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.share) {
            shareStreetView()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareStreetView() {
        val camera = presenter.cameraPosition ?: return

        val subscribe = shareUtil.createShareIntentFromStreetProjection(marker_view, camera)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    progressDialog = ProgressDialog.show(activity,
                            getString(R.string.share_create_title),
                            getString(R.string.share_create_msg),
                            false,
                            true, { subscriptions.clear() })
                }
                .doOnTerminate { progressDialog?.dismiss() }
                .subscribe({
                    startActivity(it)
                }, { Timber.e(it) })
        compositeDisposable.add(subscribe)
    }

    fun setStreetViewListeners() {
        marker_view.onMarkerClickListener = {
            presenter.onMarkerClicked(it)
        }
        marker_view.onMarkerLongClickListener = {
            presenter.onMarkerClicked(it)
        }
        marker_view.onStreetLoadedSuccess = { loadedSuccess ->
            if (!loadedSuccess) {
                presenter.errorLoadingStreetView()
            }
        }
        marker_view.onCameraUpdateListener = {
            presenter.onCameraUpdate(it)
        }
    }

    override fun renderView(viewModel: StreetViewModel) {
        with(viewModel) {
            errorRes?.let { Toast.makeText(activity, it, Toast.LENGTH_SHORT).show() }
            places?.let { marker_view.addMarkers(it.toHashSet()) }
        }
    }

    override fun onResume() {
        super.onResume()
        marker_view.onResume()
        presenter.refreshPins()
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
        compositeDisposable.clear()
        progressDialog?.takeIf { it.isShowing }?.dismiss()
        super.onDestroyView()
    }

    override fun unsubscribe() {
        presenter.unsubscribe()
    }
}