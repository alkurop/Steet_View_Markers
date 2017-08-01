package com.alkurop.mystreetplaces.di.modules.ui

import com.alkurop.mystreetplaces.data.pin.PinRepo
import com.alkurop.mystreetplaces.ui.home.MainActivityPresenter
import com.alkurop.mystreetplaces.ui.home.MainActivityPresenterImpl
import com.alkurop.mystreetplaces.ui.maps.MapPresenter
import com.alkurop.mystreetplaces.ui.maps.MapPresenterImpl
import com.alkurop.mystreetplaces.ui.pin.drop.DropPinPresenter
import com.alkurop.mystreetplaces.ui.pin.drop.DropPinPresenterImpl
import com.alkurop.mystreetplaces.ui.pin.view.PinViewPresenter
import com.alkurop.mystreetplaces.ui.pin.view.PinViewPresenterImpl
import com.alkurop.mystreetplaces.ui.places.PlacesPresenter
import com.alkurop.mystreetplaces.ui.places.PlacesPresenterImpl
import com.alkurop.mystreetplaces.ui.settings.SettingsPresenter
import com.alkurop.mystreetplaces.ui.settings.SettingsPresenterImpl
import com.alkurop.mystreetplaces.ui.street.StreetPresenterImpl
import com.alkurop.mystreetplaces.ui.street.StreetPresenter
import dagger.Module
import dagger.Provides


@Module
class PresenterModule {
    @Provides fun provideMainPresenter(): MainActivityPresenter = MainActivityPresenterImpl()

    @Provides fun provideMapPresenter(pinRepo: PinRepo): MapPresenter = MapPresenterImpl(pinRepo)

    @Provides fun provideStreetPresenter(): StreetPresenter = StreetPresenterImpl()

    @Provides fun providePlacesPresenter(): PlacesPresenter = PlacesPresenterImpl()

    @Provides fun provideSettingsPresenter(): SettingsPresenter = SettingsPresenterImpl()

    @Provides fun provideDropPinPresenter(pinRepo: PinRepo): DropPinPresenter {
        return DropPinPresenterImpl(pinRepo)
    }

    @Provides fun provideViewPinPresenter(): PinViewPresenter = PinViewPresenterImpl()
}

