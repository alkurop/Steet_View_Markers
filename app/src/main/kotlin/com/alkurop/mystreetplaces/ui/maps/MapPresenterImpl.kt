package com.alkurop.mystreetplaces.ui.maps

import com.alkurop.mystreetplaces.ui.createNavigationSubject
import com.alkurop.mystreetplaces.ui.createViewSubject
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import io.reactivex.subjects.Subject


class MapPresenterImp : MapPresenter {
    override val viewBus: Subject<MapViewModel> = createViewSubject()
    override val navBus: Subject<NavigationAction> = createNavigationSubject()

    override fun unsubscribe() {
    }
}