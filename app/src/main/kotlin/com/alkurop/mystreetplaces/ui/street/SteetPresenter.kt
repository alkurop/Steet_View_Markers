package com.alkurop.mystreetplaces.ui.street

import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import io.reactivex.subjects.Subject


interface StreetPresenter {
    val viewBus: Subject<StreetViewModel>
    val navBus: Subject<NavigationAction>

    fun unsubscribe()
}