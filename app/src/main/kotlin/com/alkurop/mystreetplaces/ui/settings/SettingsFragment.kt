package com.alkurop.mystreetplaces.ui.settings

import android.os.Bundle
import com.alkurop.mystreetplaces.ui.base.BaseMvpFragment
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import io.reactivex.Observable
import javax.inject.Inject


class SettingsFragment : BaseMvpFragment<SettingsViewModel>() {
    @Inject lateinit var presenter: SettingsPresenter

    override fun getSubject(): Observable<SettingsViewModel> = presenter.viewBus

    override fun getNavigation(): Observable<NavigationAction> = presenter.navBus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component().inject(this)
    }

    override fun renderView(viewModel: SettingsViewModel) {
    }

    override fun unsubscribe() {
        presenter.unsubscribe()
    }


}