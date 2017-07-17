package com.alkurop.mystreetplaces.ui.places

import android.os.Bundle
import com.alkurop.mystreetplaces.ui.base.BaseMvpFragment
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import io.reactivex.Observable
import javax.inject.Inject


class PlacesFragment : BaseMvpFragment<PlacesViewModel>() {
    @Inject lateinit var presenter: PlacesPresenter
    override fun getSubject(): Observable<PlacesViewModel> = presenter.viewBus

    override fun getNavigation(): Observable<NavigationAction> = presenter.navBus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component().inject(this)
    }

    override fun renderView(viewModel: PlacesViewModel) {
    }

    override fun unsubscribe() {
        presenter.unsubscribe()
    }
}