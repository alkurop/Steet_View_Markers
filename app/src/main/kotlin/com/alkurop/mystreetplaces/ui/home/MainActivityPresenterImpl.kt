package com.alkurop.mystreetplaces.ui.home

import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.ui.createNavigationSubject
import com.alkurop.mystreetplaces.ui.createViewSubject
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import io.reactivex.subjects.Subject


class MainActivityPresenterImpl : MainActivityPresenter {
    override val viewBus: Subject<MainActivityView> = createViewSubject()
    override val navBus: Subject<NavigationAction> = createNavigationSubject()

    override var currentModel: MainActivityView?  = null
    val containerId = R.id.fragment_container
    var currentTab: Int = 0


    override fun start() {
        currentModel?.let {
            viewBus.onNext(it)
        }
    }

    override fun onFragmentShowed(tag: String) {
    }

    override fun unsubscribe() {
    }

    override fun onDrawerAction(action: Int) {
        if (currentTab != action) {
            when (action) {
                R.id.map -> showMap()
                R.id.places -> showPlaces()
                R.id.settings -> showSettings()
            }
        }
    }

    private fun showSettings() {

    }

    private fun showPlaces() {

    }

    private fun showMap() {

    }
}