package com.alkurop.mystreetplaces.ui.street

import android.os.Bundle
import android.view.MenuItem
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.ui.base.BaseActivity
import com.alkurop.mystreetplaces.ui.base.BaseMvpActivity
import com.alkurop.mystreetplaces.ui.createNavigationSubject
import com.alkurop.mystreetplaces.ui.createViewSubject
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class StreetActivity : BaseMvpActivity<Any>() {
    val compositeDis = CompositeDisposable()
    override fun getSubject(): Observable<Any> {
        return createViewSubject()
    }

    override fun getNavigation(): Observable<NavigationAction> {
        return createNavigationSubject()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component().inject(this)
        setupRootView(R.layout.activity_container)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.street_view)
        val focusLocation = intent
                .getParcelableExtra<Bundle>(BaseMvpActivity.ARGS_KEY)
                .getParcelable<LatLng>(StreetFragment.FOCUS_LOCATION_KEY)
        addStreetFragment(focusLocation)
    }

    private fun addStreetFragment(focusLocation: LatLng) {
        val newInstance = StreetFragment.getNewInstance(focusLocation)
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, newInstance)
                .commit()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun unsubscribe() {
        compositeDis.clear()
    }

    override fun renderView(viewModel: Any) {
    }

}