package com.alkurop.mystreetplaces.ui.settings

import com.alkurop.mystreetplaces.ui.createNavigationSubject
import com.alkurop.mystreetplaces.ui.createViewSubject
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import io.reactivex.subjects.Subject


class SettingsPresenterImpl : SettingsPresenter {
    override val viewBus: Subject<SettingsViewModel> = createViewSubject()
    override val navBus: Subject<NavigationAction> = createNavigationSubject()

        override fun unsubscribe() {
    }
}