package com.alkurop.mystreetplaces.ui.googleplaces

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.alkurop.mystreetplaces.data.pin.PictureWrapper
import com.alkurop.mystreetplaces.data.pin.PinRepo
import com.alkurop.mystreetplaces.data.pin.mapToPin
import com.alkurop.mystreetplaces.data.search.GooglePlace
import com.alkurop.mystreetplaces.data.search.GooglePlacesSearch
import com.alkurop.mystreetplaces.domain.pin.PinDto
import com.alkurop.mystreetplaces.ui.createNavigationSubject
import com.alkurop.mystreetplaces.ui.createViewSubject
import com.alkurop.mystreetplaces.ui.navigation.ActivityNavigationAction
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.alkurop.mystreetplaces.ui.navigation.UriNavigationAction
import com.alkurop.mystreetplaces.ui.pin.drop.DropPinActivity
import com.alkurop.mystreetplaces.ui.pin.drop.DropPinFragment
import com.alkurop.mystreetplaces.ui.pin.picture.container.PictureActivity
import com.alkurop.mystreetplaces.ui.pin.picture.container.PicturePreviewContainerStateModel
import com.alkurop.mystreetplaces.ui.pin.view.PinViewPresenterImpl
import com.alkurop.mystreetplaces.ui.street.StreetActivity
import com.alkurop.mystreetplaces.ui.street.StreetFragment
import com.alkurop.mystreetplaces.utils.ShareUtil
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.maps.model.LatLng
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.Subject
import timber.log.Timber
import java.util.*

class GooglePlaceViewPresenterImpl(val pinRepo: PinRepo,
                                   val shareUtil: ShareUtil) : GooglePlaceViewPresenter {
    override val viewBus: Subject<GooglePlaceViewModel> = createViewSubject()
    override val navBus: Subject<NavigationAction> = createNavigationSubject()
    override lateinit var googleApiClient: GoogleApiClient
    override lateinit var googleSearch: GooglePlacesSearch
    val compositeDisposable = CompositeDisposable()
    lateinit var pin: PinDto

    override fun onStart(googlePlace: GooglePlace) {
        pin = googlePlace.mapToPin()
        val model = GooglePlaceViewModel(pin)
        viewBus.onNext(model)
        val subscribe = googleSearch.getPlacePicturesMetadata(googlePlace)
                .toObservable()
                .map {
                    val map = it.map {
                        val f = it.javaClass.getDeclaredField("zziee") //NoSuchFieldException
                        f.isAccessible = true
                        f.get(it) as String
                    }
                    map.forEach {
                        val wrapper = PictureWrapper()
                        wrapper.serverPhoto = it
                        wrapper.id = UUID.randomUUID().toString()
                        wrapper.timeStamp = System.currentTimeMillis()
                        pin.pictures.add(wrapper)
                    }
                    map
                }
                .subscribe({
                    val model = GooglePlaceViewModel(pin)
                    viewBus.onNext(model)
                }, {
                    Timber.e(it)
                })

        val subscribe1 = viewBus.flatMap {
            pinRepo.addTempPin(it.pin)
                    .toObservable()
        }
                .subscribeOn(Schedulers.io()).subscribe({}, { Timber.e(it) })
        compositeDisposable += subscribe1
        compositeDisposable += subscribe
    }

    override fun onPictureClick(items: List<PictureWrapper>, position: Int) {
        val stateModel = PicturePreviewContainerStateModel(items.toMutableList(), position)
        val args = Bundle()
        args.putParcelable(PictureActivity.START_MODEL_KEY, stateModel)
        val navModel = ActivityNavigationAction(PictureActivity::class.java, args, startForResult = true, requestCode = PictureActivity.REQUEST_CODE)
        navBus.onNext(navModel)
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }

    override fun onShare() {
        val shareIntent = shareUtil.createShareIntentFromPin(pin)
        val navAction = UriNavigationAction(shareIntent)
        navBus.onNext(navAction)
    }

    override fun onNavigate() {
        val uri = Uri.parse("https://www.google.com/maps/search/?api=1&query=${pin.lat},${pin.lon}")
        val shareIntent = Intent(Intent.ACTION_VIEW, uri)
        shareIntent.`package` = "com.google.android.apps.maps"
        val navAction = UriNavigationAction(shareIntent)
        navBus.onNext(navAction)
    }

    override fun onSave() {
        val args = Bundle()
        args.putString(DropPinFragment.ID_KEY, pin.id)
        val nav = ActivityNavigationAction(DropPinActivity::class.java, args, startForResult = true, requestCode = PinViewPresenterImpl.REQUEST_EDIT_CODE)
        navBus.onNext(nav)
    }

    override fun onStreet() {
        fun navigateToStreetView(latLng: LatLng) {
            val args = Bundle()
            args.putParcelable(StreetFragment.FOCUS_LOCATION_KEY, latLng)
            val navigationAction = ActivityNavigationAction(StreetActivity::class.java, args)
            navBus.onNext(navigationAction)
        }

        navigateToStreetView(LatLng(pin.lat, pin.lon))
    }
}