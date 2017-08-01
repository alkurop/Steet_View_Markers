package com.alkurop.mystreetplaces.ui.pin.view

import com.alkurop.mystreetplaces.data.pin.PinRepo
import com.alkurop.mystreetplaces.ui.createNavigationSubject
import com.alkurop.mystreetplaces.ui.createViewSubject
import com.alkurop.mystreetplaces.ui.navigation.ActivityNavigationAction
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.Subject
import timber.log.Timber

/**
 * Created by alkurop on 7/21/17.
 */
class PinViewPresenterImpl(val pinRepo: PinRepo) : PinViewPresenter {
    override val viewBus: Subject<PinViewModel> = createViewSubject()

    override val navBus: Subject<NavigationAction> = createNavigationSubject()

    val pinSubscription = CompositeDisposable()

    override fun loadPinDetails(id: String) {
        val sub = pinRepo.getPinDetails(id)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    val viewModel = PinViewModel(it)
                    viewBus.onNext(viewModel)
                }, { Timber.e(it) })
        pinSubscription.add(sub)
    }

    override fun onEdit() {
    }
    override fun unsubscribe() {
        pinSubscription.clear()
    }
}