package com.alkurop.mystreetplaces.ui.pin.picture.container

import com.alkurop.mystreetplaces.data.pin.PinRepo
import com.alkurop.mystreetplaces.ui.createNavigationSubject
import com.alkurop.mystreetplaces.ui.createViewSubject
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.Subject
import timber.log.Timber

class PicturePreviewContainerPresenterImpl(val pinRepo: PinRepo) : PicturePreviewContainerPresenter {

    override var stateModel: PicturePreviewContainerStateModel? = null
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
        stateModel?.let {
            stateModel = PicturePreviewContainerStateModel(it.picturesList, position)
        }
    }

    override fun deletePicture() {
        stateModel?.let { model ->
            val pictureIndex = model.startIndex
            val listSize = model.picturesList.size
            val item = model.picturesList[pictureIndex]

            pinRepo.deletePicture(item.id)
                    .subscribe({
                        removePictureFromUi(model)
                    }, { Timber.e(it) })

            if (listSize == 0) {
            } else if (pictureIndex == listSize - 1) {
            } else {
            }
        }
    }

    private fun removePictureFromUi(model: PicturePreviewContainerStateModel){}

}