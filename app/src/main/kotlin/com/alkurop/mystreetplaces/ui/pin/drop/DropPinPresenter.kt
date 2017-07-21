package com.alkurop.mystreetplaces.ui.pin.drop

import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import io.reactivex.subjects.Subject

/**
 * Created by alkurop on 7/21/17.
 */
interface DropPinPresenter {
        val viewBus: Subject<DropPinViewModel>
        val navBus: Subject<NavigationAction>

        fun unsubscribe()
}