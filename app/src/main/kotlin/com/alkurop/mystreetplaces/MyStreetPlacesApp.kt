package com.alkurop.mystreetplaces

import android.app.Application
import com.alkurop.mystreetplaces.di.components.ApplicationComponent
import com.alkurop.mystreetplaces.di.components.DaggerApplicationComponent
import com.alkurop.mystreetplaces.di.modules.ApplicationModule


class MyStreetPlacesApp : Application() {
  lateinit var component: ApplicationComponent

  override fun onCreate() {
    super.onCreate()
    component = buildApplicationComponent()

  }

  fun buildApplicationComponent(): ApplicationComponent {
    return DaggerApplicationComponent.builder()
        .applicationModule(ApplicationModule(this))
        .build()
  }
}
