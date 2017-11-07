package com.alkurop.mystreetplaces.ui.home

import android.os.Bundle
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.intercom.AppDataBus
import com.alkurop.mystreetplaces.ui.createNavigationSubject
import com.alkurop.mystreetplaces.ui.createViewSubject
import com.alkurop.mystreetplaces.ui.maps.MapFragment
import com.alkurop.mystreetplaces.ui.navigation.ActivityNavigationAction
import com.alkurop.mystreetplaces.ui.navigation.FragmentNavigationAction
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.alkurop.mystreetplaces.ui.places.PlacesFragment
import com.alkurop.mystreetplaces.ui.search.SearchActivity
import com.alkurop.mystreetplaces.ui.settings.SettingsFragment
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.Subject

class MainActivityPresenterImpl(val appDataBus: AppDataBus) : MainActivityPresenter {
    override val viewBus: Subject<MainActivityView> = createViewSubject()
    override val navBus: Subject<NavigationAction> = createNavigationSubject()

    override var currentModel: MainActivityView? = null
    val containerId = R.id.fragment_container
    var currentTab: Int = 0
    override var query: String = ""

    val dis = CompositeDisposable()
    var location: LatLng? = null

    override fun start() {

        val sub = Observable
                .merge(appDataBus.pinSearch.map { it.query },
                        appDataBus.googlePlacesSearch.map { it.query })
                .subscribe {
                    val model = MainActivityView(query = it, shouldShowSearch = true)
                    viewBus.onNext(model)
                    query = query
                }
        currentModel?.let {
            viewBus.onNext(it)
        }
        if (currentModel == null) {
            onDrawerAction(R.id.map)
        }

        val sub2 = appDataBus.mapLocation.subscribe({ region ->
            val latLngBounds = region.visibleRegion?.latLngBounds ?: return@subscribe
            location = latLngBounds.center

        })
        dis.addAll(sub, sub2)
    }

    override fun onDrawerAction(action: Int) {
        if (currentTab != action) {
            currentTab = action
            when (action) {
                R.id.map -> showMap()
                R.id.places -> showPlaces()
                R.id.settings -> showSettings()
            }
        }
    }

    private fun showSettings() {
        val nav = FragmentNavigationAction(SettingsFragment::class.java, containerId)
        navBus.onNext(nav)
    }

    private fun showPlaces() {
        val nav = FragmentNavigationAction(PlacesFragment::class.java, containerId)
        navBus.onNext(nav)
    }

    private fun showMap() {
        val nav = FragmentNavigationAction(MapFragment::class.java, containerId)
        navBus.onNext(nav)
    }

    override fun onFragmentShowed(tag: String) {
        val toolbarTitleRes = when (tag) {
            SettingsFragment::class.java.canonicalName -> R.string.settings
            PlacesFragment::class.java.canonicalName -> R.string.places
            MapFragment::class.java.canonicalName -> R.string.map
            else -> -1
        }
        val isShowSearch = tag == MapFragment::class.java.canonicalName
        val model = MainActivityView(toolbarTitleRes = toolbarTitleRes, shouldShowSearch = isShowSearch)
        viewBus.onNext(model)
    }

    override fun unsubscribe() {
        dis.clear()
    }

    override fun onSearchClicked() {
        if (location == null) return
        val animation = IntArray(2)
        animation[0] = R.anim.fade_in
        animation[1] = R.anim.fade_out
        val args = Bundle()
        args.putString(SearchActivity.KEY_QUERY, query)
        args.putParcelable(SearchActivity.KEY_LOCATION, location)
        val action = ActivityNavigationAction(SearchActivity::class.java,
                overrideAnimation = animation, args = args)

        navBus.onNext(action)
    }

}