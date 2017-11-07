package com.alkurop.mystreetplaces.ui.googleplaces

import com.alkurop.mystreetplaces.data.pin.PictureWrapper
import com.alkurop.mystreetplaces.data.pin.PinRepo
import com.alkurop.mystreetplaces.data.pin.mapToPin
import com.alkurop.mystreetplaces.data.search.GooglePlace
import com.alkurop.mystreetplaces.data.search.GooglePlacesSearch
import com.alkurop.mystreetplaces.domain.pin.PinDto
import com.alkurop.mystreetplaces.ui.createNavigationSubject
import com.alkurop.mystreetplaces.ui.createViewSubject
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.alkurop.mystreetplaces.utils.ShareUtil
import com.google.android.gms.common.api.GoogleApiClient
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

        val subscribe1 = viewBus.flatMap { pinRepo.addTempPin(it.pin)
                .toObservable() }
                .subscribeOn(Schedulers.io()).subscribe({},{Timber.e(it)})
        compositeDisposable += subscribe1
        compositeDisposable += subscribe
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }

    override fun onShare() {
    }

    override fun onNavigate() {
    }

    override fun onSave() {}
}