package com.alkurop.mystreetplaces.di.modules

import com.alkurop.mystreetplaces.utils.LocationTracker
import com.alkurop.mystreetplaces.utils.LocationTrackerImpl
import dagger.Module
import dagger.Provides


@Module
open class UtilityModule {

    @Provides fun provideLocationTracker(): LocationTracker {
        return LocationTrackerImpl()
    }

}
