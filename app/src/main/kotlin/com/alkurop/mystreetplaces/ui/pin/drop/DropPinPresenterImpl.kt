package com.alkurop.mystreetplaces.ui.pin.drop

import com.alkurop.mystreetplaces.data.pin.PictureWrapper
import com.alkurop.mystreetplaces.data.pin.PinRepo
import com.alkurop.mystreetplaces.domain.pin.PinDto
import com.alkurop.mystreetplaces.ui.createNavigationSubject
import com.alkurop.mystreetplaces.ui.createViewSubject
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.alkurop.mystreetplaces.ui.navigation.NoArgsNavigation
import com.google.android.gms.maps.model.LatLng
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.Subject
import timber.log.Timber
import java.io.File

class DropPinPresenterImpl(val pinRepo: PinRepo) : DropPinPresenter {
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
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }
}