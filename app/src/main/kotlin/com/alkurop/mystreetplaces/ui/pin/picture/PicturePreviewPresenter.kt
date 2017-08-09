package com.alkurop.mystreetplaces.ui.pin.picture

import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import io.reactivex.subjects.Subject

/**
 * Created by oleksii.kuropiatnyk on 08/08/2017.
 */
interface PicturePreviewPresenter {
    var viewSubject: Subject<PicturePreviewActivityModel>
    var navSubject: Subject<NavigationAction>

    fun loadContent(model: PicturePreviewStateModel)

    fun unsubscribe()
}