package com.alkurop.mystreetplaces.di.modules

import android.content.Context
import com.alkurop.mystreetplaces.utils.LocationTracker
import com.alkurop.mystreetplaces.utils.LocationTrackerImpl
import com.alkurop.mystreetplaces.utils.ShareUtil
import com.alkurop.mystreetplaces.utils.ShareUtilImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class UtilityModule {

    @Provides fun provideLocationTracker(): LocationTracker {
        return LocationTrackerImpl()
    }

    @Provides @Singleton fun provideShareUtil(context: Context):ShareUtil{
        return ShareUtilImpl(context)
    }

}
