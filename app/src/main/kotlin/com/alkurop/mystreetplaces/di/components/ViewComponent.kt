package com.alkurop.mystreetplaces.di.components

import com.alkurop.mystreetplaces.di.annotations.PerView
import com.alkurop.mystreetplaces.di.modules.ViewModule
import com.alkurop.mystreetplaces.ui.pin.drop.DropPinFragment
import com.alkurop.mystreetplaces.ui.pin.view.PinView
import dagger.Subcomponent


@PerView
@Subcomponent(modules = arrayOf(ViewModule::class))
interface ViewComponent {

    fun inject(dropPinView: DropPinFragment)
    fun inject(dropPinView: PinView)

}
