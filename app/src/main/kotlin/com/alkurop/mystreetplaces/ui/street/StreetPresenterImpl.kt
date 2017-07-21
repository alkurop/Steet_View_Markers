package com.alkurop.mystreetplaces.ui.street

import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.ui.createNavigationSubject
import com.alkurop.mystreetplaces.ui.createViewSubject
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.Subject


class StreetPresenterImpl : StreetPresenter {
    val compositeDisposable = CompositeDisposable()
    override val viewBus: Subject<StreetViewModel> = createViewSubject()
    override val navBus: Subject<NavigationAction> = createNavigationSubject()

    override fun errorLoadingStreetView() {
        val viewModel = StreetViewModel(errorRes = R.string.er_no_street_view_for_location)
        viewBus.onNext(viewModel)
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }
}