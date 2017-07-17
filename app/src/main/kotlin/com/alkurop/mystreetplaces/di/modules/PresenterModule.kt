package com.alkurop.mystreetplaces.di.modules

import com.alkurop.mystreetplaces.ui.home.MainActivityPresenter
import com.alkurop.mystreetplaces.ui.home.MainActivityPresenterImpl
import com.alkurop.mystreetplaces.ui.maps.MapPresenter
import com.alkurop.mystreetplaces.ui.maps.MapPresenterImp
import com.alkurop.mystreetplaces.ui.street.StreetPresenterImpl
import com.alkurop.mystreetplaces.ui.street.StreetPresenter
import dagger.Module
import dagger.Provides


@Module
class PresenterModule {
    @Provides fun provideMainPresenter(): MainActivityPresenter = MainActivityPresenterImpl()

    @Provides fun provideMapPresenter(): MapPresenter = MapPresenterImp()

    @Provides fun provideStreetPresenter(): StreetPresenter = StreetPresenterImpl()
}

