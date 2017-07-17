package com.alkurop.mystreetplaces.ui.home

import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.ui.createNavigationSubject
import com.alkurop.mystreetplaces.ui.createViewSubject
import com.alkurop.mystreetplaces.ui.maps.MapFragment
import com.alkurop.mystreetplaces.ui.navigation.FragmentNavigationAction
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.alkurop.mystreetplaces.ui.places.PlacesFragment
import com.alkurop.mystreetplaces.ui.settings.SettingsFragment
import io.reactivex.subjects.Subject


class MainActivityPresenterImpl : MainActivityPresenter {
    override val viewBus: Subject<MainActivityView> = createViewSubject()
    override val navBus: Subject<NavigationAction> = createNavigationSubject()

    override var currentModel: MainActivityView? = null
    val containerId = R.id.fragment_container
    var currentTab: Int = 0


    override fun start() {
        currentModel?.let {
            viewBus.onNext(it)
        }
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
            else -> throw  IllegalArgumentException("Unknown fragment")
        }
        val model = MainActivityView(toolbarTitleRes = toolbarTitleRes)
        viewBus.onNext(model)
    }

    override fun unsubscribe() {
    }
}