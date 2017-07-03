package com.alkurop.mystreetplaces.di.modules

import android.app.Service
import com.alkurop.mystreetplaces.di.annotations.PerActivity
import dagger.Module
import dagger.Provides


@Module
open class ServiceModule(val service: Service) {

  @Provides
  @PerActivity
  internal fun service(): Service {
    return service
  }

}
