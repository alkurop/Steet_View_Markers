package com.alkurop.mystreetplaces.di.modules.ui

import com.alkurop.mystreetplaces.data.pin.PinRepo
import com.alkurop.mystreetplaces.intercom.AppDataBus
import com.alkurop.mystreetplaces.ui.home.MainActivityPresenter
import com.alkurop.mystreetplaces.ui.home.MainActivityPresenterImpl
import com.alkurop.mystreetplaces.ui.maps.MapPresenter
import com.alkurop.mystreetplaces.ui.maps.MapPresenterImpl
import com.alkurop.mystreetplaces.ui.pin.drop.DropPinPresenter
import com.alkurop.mystreetplaces.ui.pin.drop.DropPinPresenterImpl
import com.alkurop.mystreetplaces.ui.pin.picture.container.PicturePreviewContainerPresenter
import com.alkurop.mystreetplaces.ui.pin.picture.container.PicturePreviewContainerPresenterImpl
import com.alkurop.mystreetplaces.ui.pin.picture.view.PreviewPicturePresenter
import com.alkurop.mystreetplaces.ui.pin.picture.view.PreviewPicturePresenterImpl
import com.alkurop.mystreetplaces.ui.pin.view.PinViewPresenter
import com.alkurop.mystreetplaces.ui.pin.view.PinViewPresenterImpl
import com.alkurop.mystreetplaces.ui.places.GooglePlaceViewPresenter
import com.alkurop.mystreetplaces.ui.places.GooglePlaceViewPresenterImpl
import com.alkurop.mystreetplaces.ui.places.PlacesPresenter
import com.alkurop.mystreetplaces.ui.places.PlacesPresenterImpl
import com.alkurop.mystreetplaces.ui.search.SearchPresenter
import com.alkurop.mystreetplaces.ui.search.SearchPresenterImpl
import com.alkurop.mystreetplaces.ui.settings.SettingsPresenter
import com.alkurop.mystreetplaces.ui.settings.SettingsPresenterImpl
import com.alkurop.mystreetplaces.ui.street.StreetPresenter
import com.alkurop.mystreetplaces.ui.street.StreetPresenterImpl
import com.alkurop.mystreetplaces.utils.AddressUtil
import com.alkurop.mystreetplaces.utils.ShareUtil
import dagger.Module
import dagger.Provides

@Module
class PresenterModule {
    @Provides
    fun provideMainPresenter(appDataBus: AppDataBus): MainActivityPresenter = MainActivityPresenterImpl(appDataBus)

    @Provides
    fun provideMapPresenter(pinRepo: PinRepo, appDataBus: AppDataBus): MapPresenter = MapPresenterImpl(pinRepo, appDataBus)

    @Provides
    fun provideStreetPresenter(pinRepo: PinRepo): StreetPresenter = StreetPresenterImpl(pinRepo)

    @Provides
    fun providePlacesPresenter(): PlacesPresenter = PlacesPresenterImpl()

    @Provides
    fun provideSettingsPresenter(): SettingsPresenter = SettingsPresenterImpl()

    @Provides
    fun provideDropPinPresenter(pinRepo: PinRepo, addressUtil: AddressUtil): DropPinPresenter = DropPinPresenterImpl(pinRepo, addressUtil)

    @Provides
    fun provideViewPinPresenter(pinRepo: PinRepo, shareUtil: ShareUtil): PinViewPresenter = PinViewPresenterImpl(pinRepo, shareUtil)

    @Provides
    fun provideViewGooglePlacePresenter(pinRepo: PinRepo, shareUtil: ShareUtil): GooglePlaceViewPresenter = GooglePlaceViewPresenterImpl(pinRepo, shareUtil)

    @Provides
    fun providePreviewPictureContainerPresenter(pinRepo: PinRepo): PicturePreviewContainerPresenter = PicturePreviewContainerPresenterImpl(pinRepo)

    @Provides
    fun providePreviewPicturePresenter(): PreviewPicturePresenter = PreviewPicturePresenterImpl()

    @Provides
    fun provideSearchPresenter(pinRepo: PinRepo, appDataBus: AppDataBus): SearchPresenter = SearchPresenterImpl(pinRepo, appDataBus)
}

