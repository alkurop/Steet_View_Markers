package com.alkurop.mystreetplaces.di.modules.ui

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.alkurop.mystreetplaces.MyStreetPlacesApp
import com.alkurop.mystreetplaces.db.RealmProvider
import com.alkurop.mystreetplaces.db.RealmProviderImpl
import com.alkurop.mystreetplaces.domain.IntercomEvent
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import io.realm.DynamicRealm
import io.realm.RealmConfiguration
import io.realm.RealmMigration
import javax.inject.Singleton

typealias IntercomBus = Subject<IntercomEvent>

@Module
open class ApplicationModule(private val application: MyStreetPlacesApp) {
    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)
    private val gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun application(): MyStreetPlacesApp {
        return application
    }

    @Provides
    @Singleton
    fun context(): Context {
        return application
    }

    @Provides
    @Singleton
    fun prefs(): SharedPreferences {
        return prefs
    }

    @Provides
    @Singleton
    fun provideIntercomBus(): IntercomBus {
        return PublishSubject.create<IntercomEvent>()
    }

    @Provides
    @Singleton open fun provideRealmProvider(): RealmProvider {
        return RealmProviderImpl(RealmConfiguration.Builder()
                .schemaVersion(1)
                .migration { realm, oldVersion, _ ->
                    if (oldVersion < 1) {
                        val schema = realm.schema
                        val pinSchema = schema.get("PinDto")
                        pinSchema.addField("categoryId", String::class.java)
                        pinSchema.addField("isFromGoogle", Boolean::class.javaPrimitiveType)
                        pinSchema.addField("isTemp", Boolean::class.javaPrimitiveType)
                    }
                }
                .build())
    }

    @Provides
    @Singleton open fun provideGson(): Gson {
        return gson
    }
}
