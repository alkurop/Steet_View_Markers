package com.alkurop.mystreetplaces.ui.pin.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.alkurop.mystreetplaces.data.pin.PictureWrapper
import com.alkurop.mystreetplaces.data.pin.PinRepo
import com.alkurop.mystreetplaces.ui.createNavigationSubject
import com.alkurop.mystreetplaces.ui.createViewSubject
import com.alkurop.mystreetplaces.ui.navigation.ActivityNavigationAction
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.alkurop.mystreetplaces.ui.navigation.UriNavigationAction
import com.alkurop.mystreetplaces.ui.pin.drop.DropPinActivity
import com.alkurop.mystreetplaces.ui.pin.drop.DropPinFragment
import com.alkurop.mystreetplaces.ui.pin.picture.container.PictureActivity
import com.alkurop.mystreetplaces.ui.pin.picture.container.PicturePreviewContainerStateModel
import com.alkurop.mystreetplaces.utils.ShareUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.Subject
import timber.log.Timber

/**
 * Created by alkurop on 7/21/17.
 */
class PinViewPresenterImpl(val pinRepo: PinRepo, val shareUtil: ShareUtil) : PinViewPresenter {
    companion object {
        val REQUEST_EDIT_CODE = 8000
    }

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
        val nav = ActivityNavigationAction(DropPinActivity::class.java, args, startForResult = true, requestCode = REQUEST_EDIT_CODE)
        navBus.onNext(nav)
    }

    override fun unsubscribe() {
        pinSubscription.clear()
    }

    override fun onPictureClick(items: List<PictureWrapper>, position: Int) {
        val stateModel = PicturePreviewContainerStateModel(items.toMutableList(), position)
        val args = Bundle()
        args.putParcelable(PictureActivity.START_MODEL_KEY, stateModel)
        val navModel = ActivityNavigationAction(PictureActivity::class.java, args, startForResult = true, requestCode = PictureActivity.REQUEST_CODE)
        navBus.onNext(navModel)
    }

    override fun onNavigate() {
        val subscribe = pinRepo.getPinDetails(id).subscribe({
            val uri = Uri.parse("http://maps.google.com/maps?daddr=${it.lat},${it.lon}(${it.title}")
            val shareIntent = Intent(Intent.ACTION_VIEW, uri)
            shareIntent.`package` = "com.google.android.apps.maps"
            val navAction = UriNavigationAction(shareIntent)
            navBus.onNext(navAction)
        }, { Timber.e(it) })
        pinSubscription.add(subscribe)

    }

    override fun onShare() {
        val subscribe = pinRepo.getPinDetails(id).subscribe({
            val shareIntent = shareUtil.createShareIntentFromPin(it)
            val navAction = UriNavigationAction(shareIntent)
            navBus.onNext(navAction)
        }, { Timber.e(it) })
        pinSubscription.add(subscribe)
    }
}