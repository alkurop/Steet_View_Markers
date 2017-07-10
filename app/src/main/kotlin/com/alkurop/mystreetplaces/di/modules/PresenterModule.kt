package com.alkurop.mystreetplaces.di.modules

import com.alkurop.mystreetplaces.ui.home.MainActivityPresenter
import com.alkurop.mystreetplaces.ui.home.MainActivityPresenterImpl
import dagger.Module
import dagger.Provides


@Module
class PresenterModule {
    @Provides fun provideMainPresenter(): MainActivityPresenter = MainActivityPresenterImpl()
}

