package com.alkurop.mystreetplaces

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.alkurop.mystreetplaces.di.components.ApplicationComponent
import com.alkurop.mystreetplaces.di.components.DaggerApplicationComponent
import com.alkurop.mystreetplaces.di.modules.ui.ApplicationModule
import com.facebook.stetho.Stetho
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.ImageScaleType
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import io.realm.Realm
import timber.log.Timber

class MyStreetPlacesApp : Application() {
    lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        component = buildApplicationComponent()
        initRealm()
        initStetho()
        initUniversalImageLoader()
    }

    fun installMultiDex() {
        MultiDex.install(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        installMultiDex()
    }

    fun buildApplicationComponent(): ApplicationComponent {
        return DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }

    fun initStetho() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
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

    fun initUniversalImageLoader() {
        val defaultOptions = DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheOnDisk(true)
                .build()
        val config = ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(defaultOptions)
                .build()
        ImageLoader.getInstance().init(config)
    }
}
