package com.alkurop.mystreetplaces.ui.pin.picture.view

import com.alkurop.mystreetplaces.data.pin.PictureWrapper
import com.alkurop.mystreetplaces.ui.createNavigationSubject
import com.alkurop.mystreetplaces.ui.createViewSubject
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import io.reactivex.subjects.Subject

class PreviewPicturePresenterImpl : PreviewPicturePresenter {
    override val viewSubject: Subject<PreviewPictureModel> = createViewSubject()
    override val navSubject: Subject<NavigationAction> = createNavigationSubject()

    override fun start(pictureWrapper: PictureWrapper) {
        val model = PreviewPictureModel(pictureWrapper)
        viewSubject.onNext(model)
    }

    override fun unsubscribe() {

    }
}