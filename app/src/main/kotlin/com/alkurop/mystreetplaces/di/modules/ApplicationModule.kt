package com.alkurop.mystreetplaces.di.modules

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.alkurop.mystreetplaces.MyStreetPlacesApp
import com.alkurop.mystreetplaces.domain.IntercomEvent
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Singleton

typealias IntercomBus = Subject<IntercomEvent>

@Module
open class ApplicationModule(private val application: MyStreetPlacesApp) {
  private val prefs: SharedPreferences

  companion object {

    private val PREF_SESSION = "session_token"
  }

  init {
    this.prefs = PreferenceManager.getDefaultSharedPreferences(application)
  }

  @Provides @Singleton
  fun application(): MyStreetPlacesApp {
    return application
  }

  @Provides @Singleton
  fun context(): Context {
    return application
  }

  @Provides @Singleton
  fun prefs(): SharedPreferences {
    return prefs
  }


  @Provides @Singleton
  fun btAdapter(): BluetoothAdapter? {
    return BluetoothAdapter.getDefaultAdapter()
  }

  @Provides @Singleton fun provideIntercomBus(): IntercomBus {
    return PublishSubject.create<IntercomEvent>()
  }

  /*@Provides @Singleton open fun provideRealmProvider(): RealmProvider {
    return RealmProviderImpl(RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build())
  }*/
}
