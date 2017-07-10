package com.alkurop.mystreetplaces

import android.app.Application
import com.alkurop.mystreetplaces.di.components.ApplicationComponent
import com.alkurop.mystreetplaces.di.components.DaggerApplicationComponent
import com.alkurop.mystreetplaces.di.modules.ApplicationModule
import com.facebook.stetho.Stetho
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import io.realm.Realm


class MyStreetPlacesApp : Application() {
  lateinit var component: ApplicationComponent

  override fun onCreate() {
    super.onCreate()
    component = buildApplicationComponent()
    initRealm()
    initStetho()
  }

  fun buildApplicationComponent(): ApplicationComponent {
    return DaggerApplicationComponent.builder()
        .applicationModule(ApplicationModule(this))
        .build()
  }

  fun initStetho() {
    if (BuildConfig.DEBUG) {
      Stetho.initialize(
          Stetho.newInitializerBuilder(this)
              .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
              .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
              .build())
    }
  }

  fun initRealm() {
    Realm.init(this)
  }
}
