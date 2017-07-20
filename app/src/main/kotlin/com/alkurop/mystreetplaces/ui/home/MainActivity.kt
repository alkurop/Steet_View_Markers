package com.alkurop.mystreetplaces.ui.home

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.ActionBarDrawerToggle
import android.widget.Toast
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.ui.base.BaseMvpActivity
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.alkurop.mystreetplaces.utils.LocationTracker
import com.alkurop.mystreetplaces.utils.MapLocationSource
import com.google.android.gms.maps.LocationSource
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : BaseMvpActivity<MainActivityView>(), MapLocationSource {
    companion object {

        val MODEL_KEY = "model"
    }

    @Inject lateinit var locationTracker: LocationTracker

    @Inject lateinit var presenter: MainActivityPresenter

    var backStackListener = FragmentManager.OnBackStackChangedListener {
        val fragmentList = supportFragmentManager.fragments
        fragmentList?.filter { it != null }?.forEach {
            presenter.onFragmentShowed(it.tag)
            return@forEach
        }
    }

    override fun getSubject(): Observable<MainActivityView> {
        return presenter.viewBus
    }

    override fun getNavigation(): Observable<NavigationAction> {
        return presenter.navBus
    }

    override fun deactivate() {
        locationTracker.deactivate()
    }

    override fun activate(listener: LocationSource.OnLocationChangedListener) {
        locationTracker.activate(listener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component().inject(this)
        setupRootView(R.layout.activity_main)
        initDrawerComponents()
        locationTracker.setUp(this, { Toast.makeText(this, "Location failed", Toast.LENGTH_SHORT).show() })
        supportFragmentManager.addOnBackStackChangedListener(backStackListener)
        presenter.currentModel = savedInstanceState?.getParcelable<MainActivityView>(MODEL_KEY)
        presenter.start()
    }


    private fun initDrawerComponents() {
        navigationView.setNavigationItemSelectedListener {
            presenter.onDrawerAction(it.itemId)
            drawer_layout.closeDrawers()
            true
        }

        val mDrawerToggle = ActionBarDrawerToggle(
                this,
                drawer_layout,
                toolbar,
                R.string.app_name,
                R.string.app_name
        )
        drawer_layout.addDrawerListener(mDrawerToggle)
        mDrawerToggle.syncState()
        supportActionBar?.setHomeButtonEnabled(true)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        val bundle = outState ?: Bundle()
        bundle.putParcelable(MODEL_KEY, presenter.currentModel)
        super.onSaveInstanceState(bundle)
    }

    override fun renderView(viewModel: MainActivityView) {
        with(viewModel) {
            viewModel.toolbarTitleRes.takeIf { it != -1 }?.let { supportActionBar?.title = getString(viewModel.toolbarTitleRes) }
        }
    }

    override fun getLastKnownLocation(listener: (Location) -> Unit) {
        locationTracker.getLastKnownLocation(listener)
    }

    override fun unsubscribe() {
        presenter.unsubscribe()
    }

    override fun onDestroy() {
        supportFragmentManager.removeOnBackStackChangedListener(backStackListener)
        super.onDestroy()
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        locationTracker.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
