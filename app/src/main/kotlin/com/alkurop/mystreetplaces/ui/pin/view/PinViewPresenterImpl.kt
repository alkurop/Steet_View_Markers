package com.alkurop.mystreetplaces.ui.pin.view

import com.alkurop.mystreetplaces.ui.createNavigationSubject
import com.alkurop.mystreetplaces.ui.createViewSubject
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import io.reactivex.subjects.Subject

/**
 * Created by alkurop on 7/21/17.
 */
class PinViewPresenterImpl : PinViewPresenter {
    override val viewBus: Subject<PinViewModel> = createViewSubject()
    override val navBus: Subject<NavigationAction> = createNavigationSubject()

    override fun unsubscribe() {
    }
}