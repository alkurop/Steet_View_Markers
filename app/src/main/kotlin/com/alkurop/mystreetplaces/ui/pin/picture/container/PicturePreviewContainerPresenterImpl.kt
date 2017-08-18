package com.alkurop.mystreetplaces.ui.pin.picture.container

import com.alkurop.mystreetplaces.data.pin.PinRepo
import com.alkurop.mystreetplaces.ui.createNavigationSubject
import com.alkurop.mystreetplaces.ui.createViewSubject
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.alkurop.mystreetplaces.ui.navigation.NoArgsNavigation
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.Subject
import timber.log.Timber

class PicturePreviewContainerPresenterImpl(val pinRepo: PinRepo) : PicturePreviewContainerPresenter {

    override lateinit var stateModel: PicturePreviewContainerStateModel
    override var viewSubject: Subject<PicturePreviewContainerStateModel> = createViewSubject()
    override var navSubject: Subject<NavigationAction> = createNavigationSubject()
    val compositeDisposable = CompositeDisposable()

    override fun loadContent(model: PicturePreviewContainerStateModel) {
        viewSubject.onNext(model)
        stateModel = model
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }

    override fun onPagerPageChanged(position: Int) {
        stateModel.let {
            stateModel = PicturePreviewContainerStateModel(it.picturesList, position)
        }
    }

    override fun deletePicture() {
        stateModel.let { (picturesList, pictureIndex) ->
            val item = picturesList[pictureIndex]
            val toMutableList = stateModel.picturesList.toMutableList()
            toMutableList.removeAt(pictureIndex)
            stateModel = PicturePreviewContainerStateModel(toMutableList, stateModel.startIndex)
            pinRepo.deletePicture(item.id)
                    .subscribe({
                        if (stateModel.picturesList.isEmpty()) {
                            navSubject.onNext(NoArgsNavigation.BACK_ACTION)
                        }
                    }, { Timber.e(it) })

        }
    }
}