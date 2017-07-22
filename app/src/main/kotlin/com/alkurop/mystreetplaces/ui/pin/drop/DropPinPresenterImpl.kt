package com.alkurop.mystreetplaces.ui.pin.drop

import com.alkurop.mystreetplaces.domain.pin.PinDto
import com.alkurop.mystreetplaces.domain.pin.PinLocationDto
import com.alkurop.mystreetplaces.ui.createNavigationSubject
import com.alkurop.mystreetplaces.ui.createViewSubject
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.google.android.gms.maps.model.LatLng
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.Subject
import java.util.*


class DropPinPresenterImpl : DropPinPresenter {
    override val viewBus: Subject<DropPinViewModel> = createViewSubject()
    override val navBus: Subject<NavigationAction> = createNavigationSubject()
    val compositeDisposable = CompositeDisposable()

    override lateinit var pinDto: PinDto

    override fun start(location: LatLng) {
        pinDto = PinDto(id = UUID.randomUUID().toString(),
                location = PinLocationDto(location.latitude, location.longitude),
                title = "",
                description = "")
        val model = DropPinViewModel(pinDto = pinDto)
        viewBus.onNext(model)
    }

    override fun onTitleChange(title: String) {
        pinDto.title = title
    }

    override fun onDescriptionChange(title: String) {
        pinDto.description = title
    }

    override fun submit() {}

    override fun unsubscribe() {
        compositeDisposable.clear()
    }
}