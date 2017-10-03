package com.alkurop.mystreetplaces.di.modules

import android.content.Context
import com.alkurop.mystreetplaces.intercom.AppDataBus
import com.alkurop.mystreetplaces.utils.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class UtilityModule {
    val searchBus = AppDataBus()

    @Provides
    fun provideLocationTracker(): LocationTracker {
        return LocationTrackerImpl()
    }

    @Provides
    @Singleton
    fun provideShareUtil(context: Context): ShareUtil {
        return ShareUtilImpl(context)
    }

    @Provides
    fun provideAddressUtil(context: Context): AddressUtil {
        return AddressUtilImpl(context)
    }

    @Provides
    fun provideSearchBus() = searchBus
}
