package com.alkurop.mystreetplaces.ui.pin.picture.view

import com.alkurop.mystreetplaces.data.pin.PictureWrapper
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import io.reactivex.subjects.Subject

interface PreviewPicturePresenter {

    val viewSubject:Subject<PreviewPictureModel>
    val navSubject:Subject<NavigationAction>

    fun start(pictureWrapper: PictureWrapper)
    fun unsubscribe()
}