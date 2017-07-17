package com.alkurop.mystreetplaces.ui.street

import android.os.Bundle
import com.alkurop.mystreetplaces.ui.base.BaseMvpFragment
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import io.reactivex.Observable
import javax.inject.Inject


class StreetFragment : BaseMvpFragment<StreetViewModel>() {
    @Inject lateinit var presenter: StreetPresenter
    override fun getSubject(): Observable<StreetViewModel> = presenter.viewBus

    override fun getNavigation(): Observable<NavigationAction> = presenter.navBus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component().inject(this)
    }

    override fun renderView(viewModel: StreetViewModel) {
    }

    override fun unsubscribe() {
        presenter.unsubscribe()
    }
}