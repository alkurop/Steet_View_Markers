package com.alkurop.mystreetplaces.ui.places

import com.alkurop.mystreetplaces.ui.createNavigationSubject
import com.alkurop.mystreetplaces.ui.createViewSubject
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import io.reactivex.subjects.Subject


class PlacesPresenterImpl : PlacesPresenter {
    override val viewBus: Subject<PlacesViewModel> = createViewSubject()
    override val navBus: Subject<NavigationAction> = createNavigationSubject()

    override fun unsubscribe() {
    }
}