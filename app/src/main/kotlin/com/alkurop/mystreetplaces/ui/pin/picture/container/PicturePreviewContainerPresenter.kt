package com.alkurop.mystreetplaces.ui.pin.picture.container

import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import io.reactivex.subjects.Subject

/**
 * Created by oleksii.kuropiatnyk on 08/08/2017.
 */
interface PicturePreviewContainerPresenter {
    var viewSubject: Subject<PicturePreviewContainerStateModel>
    var navSubject: Subject<NavigationAction>
    var stateModel: PicturePreviewContainerStateModel?

    fun loadContent(model: PicturePreviewContainerStateModel)

    fun unsubscribe()

    fun onPagerPageChanged(position: Int)

    fun deletePicture()
}