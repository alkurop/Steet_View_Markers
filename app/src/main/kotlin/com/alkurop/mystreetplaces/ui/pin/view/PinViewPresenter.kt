package com.alkurop.mystreetplaces.ui.pin.view

import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import io.reactivex.subjects.Subject

/**
 * Created by alkurop on 7/21/17.
 */
interface PinViewPresenter {
    val viewBus: Subject<PinViewModel>
    val navBus: Subject<NavigationAction>

    fun loadPinDetails(id: String)

    fun unsubscribe()
}