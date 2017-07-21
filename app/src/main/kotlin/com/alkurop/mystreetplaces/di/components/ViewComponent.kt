package com.alkurop.mystreetplaces.di.components

import com.alkurop.mystreetplaces.di.annotations.PerView
import com.alkurop.mystreetplaces.di.modules.ViewModule
import com.alkurop.mystreetplaces.ui.pin.drop.DropPinView
import com.alkurop.mystreetplaces.ui.pin.view.PinView
import dagger.Subcomponent


@PerView
@Subcomponent(modules = arrayOf(ViewModule::class))
interface ViewComponent {

    fun inject(dropPinView: DropPinView)
    fun inject(dropPinView: PinView)

}
