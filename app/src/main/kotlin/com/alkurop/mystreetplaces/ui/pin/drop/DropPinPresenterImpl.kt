package com.alkurop.mystreetplaces.ui.pin.drop

import android.location.Address
import android.os.Bundle
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.data.pin.AddressDto
import com.alkurop.mystreetplaces.data.pin.PictureWrapper
import com.alkurop.mystreetplaces.data.pin.PinRepo
import com.alkurop.mystreetplaces.domain.pin.PinDto
import com.alkurop.mystreetplaces.ui.createNavigationSubject
import com.alkurop.mystreetplaces.ui.createViewSubject
import com.alkurop.mystreetplaces.ui.navigation.ActivityNavigationAction
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.alkurop.mystreetplaces.ui.navigation.NoArgsNavigation
import com.alkurop.mystreetplaces.ui.pin.picture.container.PictureActivity
import com.alkurop.mystreetplaces.ui.pin.picture.container.PicturePreviewContainerStateModel
import com.alkurop.mystreetplaces.utils.AddressUtil
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.Subject
import timber.log.Timber
import java.io.File

class DropPinPresenterImpl(val pinRepo: PinRepo, val addressUtil: AddressUtil) : DropPinPresenter {
    override val viewBus: Subject<DropPinViewModel> = createViewSubject()

    override val navBus: Subject<NavigationAction> = createNavigationSubject()
    val compositeDisposable = CompositeDisposable()
    override lateinit var pinDto: PinDto

    override fun start(location: LatLng) {
        pinDto = PinDto(
                location = location,
                title = "",
                description = "")
        val model = DropPinViewModel(pinDto = pinDto)
        viewBus.onNext(model)
        lookUpAddressInternal()
    }

    override fun start(pinId: String) {
        val sub = pinRepo.getPinDetails(pinId)
                .subscribe({
                    pinDto = it
                    val model = DropPinViewModel(pinDto = pinDto)
                    viewBus.onNext(model)
                }, { Timber.e(it) })
        compositeDisposable.add(sub)
    }

    override fun onTitleChange(title: String) {
        pinDto.title = title
    }

    override fun onDescriptionChange(title: String) {
        pinDto.description = title
    }

    override fun submit() {
        if (pinDto.title.isNullOrBlank()) return
        val sub = pinRepo.addOrUpdatePin(pinDto)
                .subscribe({
                    val navigation = NoArgsNavigation.BACK_ACTION
                    navBus.onNext(navigation)
                }, { Timber.e(it) })
        compositeDisposable.add(sub)
    }

    override fun deletePin() {
        val sub = pinRepo.removePin(pinDto)
                .subscribe({
                    val nav = NoArgsNavigation.BACK_ACTION
                    navBus.onNext(nav)
                }, { Timber.e(it) })
        compositeDisposable.add(sub)
    }

    override fun onAddPicture(file: File) {
        pinDto.pictures.add(PictureWrapper("file://${file.absolutePath}"))
        if (pinDto.id != null) {
            val sub = pinRepo.updateLocalPictures(pinDto)
                    .subscribe({}, { Timber.e(it) })
            compositeDisposable.add(sub)
        }
        Timber.d("picture added ${file.absoluteFile}")
        val model = DropPinViewModel(pinDto)
        viewBus.onNext(model)
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }

    override fun onPictureClick(position: Int, items: List<PictureWrapper>) {
        val stateModel = PicturePreviewContainerStateModel(items, position)
        val args = Bundle()
        args.putParcelable(PictureActivity.START_MODEL_KEY, stateModel)
        val navModel = ActivityNavigationAction(PictureActivity::class.java, args, startForResult = true, requestCode = PictureActivity.REQUEST_CODE)
        navBus.onNext(navModel)
    }

    override fun reloadPictureList(pictures: List<PictureWrapper>) {
        pinDto.pictures.clear()
        pinDto.pictures.addAll(pictures)
        val model = DropPinViewModel(pinDto)
        viewBus.onNext(model)
    }

    override fun lookForAddress() {
        val subscribe = addressUtil
                .getAddress(LatLng(pinDto.lat, pinDto.lon))
                .doOnSubscribe {
                    val model = DropPinViewModel(isLoading = true)
                    viewBus.onNext(model)
                }
                .doAfterTerminate {
                    val model = DropPinViewModel(isLoading = false)
                    viewBus.onNext(model)
                }
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {
                            Timber.d(it.toString())
                            if (it.isEmpty()) {
                                throw IllegalStateException("Address list came empty")
                            }
                            val model = DropPinViewModel(addressList = it)
                            viewBus.onNext(model)
                        },
                        {
                            val model = DropPinViewModel(errorRes = R.string.loading_address_failed, isLoading = false)
                            viewBus.onNext(model)
                        })
        compositeDisposable.addAll(subscribe)

    }

    private fun lookUpAddressInternal() {

        val subscribe = addressUtil
                .getAddress(LatLng(pinDto.lat, pinDto.lon))
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {
                            Timber.d(it.toString())
                            it.takeIf { it.isNotEmpty() }
                                    ?.let { onAddressSelected(it[0]) }
                        },
                        {
                            Timber.e(it)
                        })
        compositeDisposable.addAll(subscribe)
    }

    override fun onAddressSelected(address: Address) {
        pinDto.address = AddressDto(address)
        val model = DropPinViewModel(pinDto)
        viewBus.onNext(model)
    }
}
