package com.alkurop.mystreetplaces.di.components

import com.alkurop.mystreetplaces.di.annotations.PerFragment
import com.alkurop.mystreetplaces.di.modules.FragmentModule
import com.alkurop.mystreetplaces.ui.maps.MapFragment
import com.alkurop.mystreetplaces.ui.pin.drop.DropPinFragment
import com.alkurop.mystreetplaces.ui.places.PlacesFragment
import com.alkurop.mystreetplaces.ui.settings.SettingsFragment
import com.alkurop.mystreetplaces.ui.street.StreetFragment
import dagger.Subcomponent


@PerFragment
@Subcomponent(modules = arrayOf(FragmentModule::class))
interface FragmentComponent {

    fun inject(fragment: MapFragment)

    fun inject(fragment: StreetFragment)

    fun inject(fragment: PlacesFragment)

    fun inject(fragment: SettingsFragment)

    fun inject(dropPinView: DropPinFragment)


}
