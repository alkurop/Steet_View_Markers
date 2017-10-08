package com.alkurop.mystreetplaces.ui.googleplaces

import com.alkurop.mystreetplaces.data.pin.PinRepo
import com.alkurop.mystreetplaces.data.search.GooglePlace
import com.alkurop.mystreetplaces.data.search.GooglePlacesSearch
import com.alkurop.mystreetplaces.ui.createNavigationSubject
import com.alkurop.mystreetplaces.ui.createViewSubject
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.alkurop.mystreetplaces.utils.ShareUtil
import com.google.android.gms.common.api.GoogleApiClient
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.Subject

class GooglePlaceViewPresenterImpl(val pinRepo: PinRepo,
                                   val shareUtil: ShareUtil) : GooglePlaceViewPresenter {
    override val viewBus: Subject<GooglePlaceViewModel> = createViewSubject()
    override val navBus: Subject<NavigationAction> = createNavigationSubject()
    override lateinit var googleApiClient: GoogleApiClient
    override lateinit var googleSearch: GooglePlacesSearch
    val compositeDisposable = CompositeDisposable()

    override fun onStart(googlePlace: GooglePlace) {
    }

    override fun unsubscribe() {
    }

    override fun onShare() {
    }

    override fun onNavigate() {
        compositeDisposable.clear()
    }

    override fun onSave() {}
}