package com.alkurop.mystreetplaces.ui.maps

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.ui.base.BaseMvpFragment
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.github.alkurop.jpermissionmanager.PermissionOptionalDetails
import com.github.alkurop.jpermissionmanager.PermissionsManager
import com.google.android.gms.maps.LocationSource
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_map.*
import javax.inject.Inject


class MapFragment : BaseMvpFragment<MapViewModel>() {
    @Inject lateinit var presenter: MapPresenter
    lateinit var permissionManager: PermissionsManager

    override fun getSubject(): Observable<MapViewModel> = presenter.viewBus

    override fun getNavigation(): Observable<NavigationAction> = presenter.navBus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component().inject(this)
        setUpPermissionsManager()
    }

    private fun setUpPermissionsManager() {
        permissionManager = PermissionsManager(this)
        val permission1 = Pair(Manifest.permission.ACCESS_COARSE_LOCATION,
                PermissionOptionalDetails( "Location",
                         "This permission is optional. I can live without it"))

        permissionManager.addPermissions(mapOf(permission1))
        permissionManager.addPermissionsListener {
            val pa = it
            for (pair in it) {
                if (!pair.value)
                    return@addPermissionsListener
            }
            initLocationTracking()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
    }

    private fun initLocationTracking() {
        mapView.getMapAsync {
            it.isMyLocationEnabled = true
            it.setLocationSource(activity as LocationSource)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        mapView.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
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

    override fun renderView(viewModel: MapViewModel) {
    }

    override fun unsubscribe() {
        presenter.unsubscribe()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        permissionManager.onActivityResult(requestCode)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
