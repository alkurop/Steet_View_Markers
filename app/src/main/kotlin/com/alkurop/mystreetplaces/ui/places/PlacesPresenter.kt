package com.alkurop.mystreetplaces.ui.places

import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import io.reactivex.subjects.Subject


interface PlacesPresenter {
    val viewBus: Subject<PlacesViewModel>
    val navBus: Subject<NavigationAction>

    fun unsubscribe()
}