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
            val item = model.picturesList[pictureIndex]
            pinRepo.deletePicture(item.id)
                    .subscribe({
                        removePictureFromUi(model, pictureIndex)
                    }, { Timber.e(it) })

        }
    }

    private fun removePictureFromUi(model: PicturePreviewContainerStateModel, pictureIndex: Int) {
        val listSize = model.picturesList.size

        when {
            listSize == 1 -> {
                val newModel = PicturePreviewContainerStateModel(listOf(), 0)
                stateModel = newModel
                val navigationAction = NoArgsNavigation.BACK_ACTION
                navSubject.onNext(navigationAction)
            }
            else -> {
                val newList = stateModel?.picturesList?.toMutableList()
                newList?.removeAt(pictureIndex)
                newList?.let {
                    val newIndex = if (pictureIndex < listSize + 1) pictureIndex - 1 else 0
                    val newModel = PicturePreviewContainerStateModel(newList, newIndex)
                    stateModel = newModel
                    viewSubject.onNext(newModel)
                }
            }
        }
    }
}