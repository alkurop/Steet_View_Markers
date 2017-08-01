package com.alkurop.mystreetplaces.ui.maps

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.data.pin.PinPlace
import com.alkurop.mystreetplaces.ui.base.BaseMvpFragment
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.alkurop.mystreetplaces.utils.LocationTracker
import com.github.alkurop.jpermissionmanager.PermissionOptionalDetails
import com.github.alkurop.jpermissionmanager.PermissionsManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_map.*
import timber.log.Timber
import javax.inject.Inject

class MapFragment : BaseMvpFragment<MapViewModel>() {

    @Inject lateinit var presenter: MapPresenter
    lateinit var permissionManager: PermissionsManager
    @Inject lateinit var locationTracker: LocationTracker
    lateinit var clusterManager: ClusterManager<MapClusterItem>

    val compositeDisposable = CompositeDisposable()
    val DEFAULT_CAMERA_ZOOM = 14f

    override fun getSubject(): Observable<MapViewModel> = presenter.viewBus

    override fun getNavigation(): Observable<NavigationAction> = presenter.navBus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component().inject(this)
        setHasOptionsMenu(true)
    }

    private fun setUpPermissionsManager() {
        permissionManager = PermissionsManager(this)
        val permission1 = Pair(Manifest.permission.ACCESS_FINE_LOCATION,
                PermissionOptionalDetails(getString(R.string.location_permission_rationale_title),
                        getString(R.string.location_permission_rationale)))

        permissionManager.addPermissions(mapOf(permission1))
        permissionManager.addPermissionsListener {
            for (pair in it) {
                if (!pair.value)
                    return@addPermissionsListener
            }
            presenter.isPermissionGranted = true
            initLocationTracking()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpPermissionsManager()
        mapView.onCreate(savedInstanceState)
        fab.setOnClickListener { presenter.onGoToStreetView() }
    }

    fun initClusterManager(map: GoogleMap) {
        clusterManager = ClusterManager(activity, map)
        val renderer = ClusterRenderer(activity, map, clusterManager)
        clusterManager.renderer = renderer
        map.setOnMarkerClickListener(clusterManager)
        map.setOnInfoWindowClickListener(clusterManager)
        map.setOnCameraIdleListener { clusterManager.onCameraIdle() }
        renderer.setOnClusterItemInfoWindowClickListener { presenter.onPinClick(it) }
    }

    private fun initLocationTracking() {
        locationTracker.setUp(activity, {
            Timber.e("Location tracking failed")
            Toast.makeText(activity, R.string.er_location_tracking_failed, Toast.LENGTH_SHORT).show()
        })
        presenter.locationTracker = locationTracker
        mapView.getMapAsync { map ->
            map.setLocationSource(locationTracker)
            map.isMyLocationEnabled = true
            map.isBuildingsEnabled = true
            map.setOnMapLoadedCallback {
                presenter.onCameraPositionChanged(map.projection.visibleRegion)
                if (map.cameraPosition.target.latitude == 0.0) {
                    val dis = locationTracker.getLastKnownLocation().firstElement().subscribe({
                        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), DEFAULT_CAMERA_ZOOM)
                        map.moveCamera(cameraUpdate)
                    })
                    compositeDisposable.add(dis)
                }
            }
            map.setOnCameraMoveListener {
                presenter.onCameraPositionChanged(map.projection.visibleRegion)
            }
            initClusterManager(map)

        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        val bundle = outState ?: Bundle()
        mapView.onSaveInstanceState(bundle)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
        presenter.isPermissionGranted = false
        permissionManager.makePermissionRequest()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.map_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.addMarker) {
            presenter.onAddMarker()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun renderView(viewModel: MapViewModel) {
        with(viewModel) {
            shouldAskForPermission.takeIf { it }?.let { permissionManager.makePermissionRequest() }
            errorRes?.let { Toast.makeText(activity, it, Toast.LENGTH_SHORT).show() }
            pins.takeIf { it.isEmpty().not() }?.let { items ->
                val clusterItems = items.map { MapClusterItem(PinPlace(it)) }
                clusterManager.addItems(clusterItems)
                clusterManager.cluster()
            }
        }
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
        presenter.unsubscribe()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        permissionManager.onActivityResult(requestCode)
        locationTracker.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
