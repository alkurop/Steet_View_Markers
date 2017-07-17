package com.alkurop.mystreetplaces.ui.settings

import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import io.reactivex.subjects.Subject


interface SettingsPresenter {
    val viewBus: Subject<SettingsViewModel>
    val navBus: Subject<NavigationAction>

    fun unsubscribe()
}