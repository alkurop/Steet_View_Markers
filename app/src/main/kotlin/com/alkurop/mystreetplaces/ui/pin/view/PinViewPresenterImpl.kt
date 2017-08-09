package com.alkurop.mystreetplaces.ui.pin.view

import android.os.Bundle
import com.alkurop.mystreetplaces.data.pin.PictureWrapper
import com.alkurop.mystreetplaces.data.pin.PinRepo
import com.alkurop.mystreetplaces.ui.createNavigationSubject
import com.alkurop.mystreetplaces.ui.createViewSubject
import com.alkurop.mystreetplaces.ui.navigation.ActivityNavigationAction
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.alkurop.mystreetplaces.ui.pin.drop.DropPinActivity
import com.alkurop.mystreetplaces.ui.pin.drop.DropPinFragment
import com.alkurop.mystreetplaces.ui.pin.picture.PictureActivity
import com.alkurop.mystreetplaces.ui.pin.picture.PicturePreviewStateModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.Subject
import timber.log.Timber
import java.io.File

/**
 * Created by alkurop on 7/21/17.
 */
class PinViewPresenterImpl(val pinRepo: PinRepo) : PinViewPresenter {
    override val viewBus: Subject<PinViewModel> = createViewSubject()

    override val navBus: Subject<NavigationAction> = createNavigationSubject()

    val pinSubscription = CompositeDisposable()
    lateinit var id: String

    override fun loadPinDetails(id: String) {
        this.id = id
        val sub = pinRepo.getPinDetails(id)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    val viewModel = PinViewModel(it)
                    viewBus.onNext(viewModel)
                }, { Timber.e(it) })
        pinSubscription.add(sub)
    }

    override fun onEdit() {
        val args = Bundle()
        args.putString(DropPinFragment.ID_KEY, id)
        val nav = ActivityNavigationAction(DropPinActivity::class.java, args)
        navBus.onNext(nav)
    }

    override fun unsubscribe() {
        pinSubscription.clear()
    }

    override fun onPictureClick(items: List<PictureWrapper>, position: Int) {
        val stateModel = PicturePreviewStateModel(items, position)
        val args = Bundle()
        args.putParcelable(PictureActivity.START_MODEL_KEY, stateModel)
        val navModel = ActivityNavigationAction(PictureActivity::class.java, args)
        navBus.onNext(navModel)
    }
}