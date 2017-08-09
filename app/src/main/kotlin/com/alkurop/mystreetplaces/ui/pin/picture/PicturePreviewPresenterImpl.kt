package com.alkurop.mystreetplaces.ui.pin.picture

import com.alkurop.mystreetplaces.data.pin.PinRepo
import com.alkurop.mystreetplaces.ui.createNavigationSubject
import com.alkurop.mystreetplaces.ui.createViewSubject
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.Subject

class PicturePreviewPresenterImpl(val pinRepo: PinRepo) : PicturePreviewPresenter {

    override var viewSubject: Subject<PicturePreviewActivityModel> = createViewSubject()
    override var navSubject: Subject<NavigationAction> = createNavigationSubject()
    val compositeDisposable = CompositeDisposable()

    override fun loadContent(model: PicturePreviewStateModel) {
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }
}