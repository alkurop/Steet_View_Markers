package com.alkurop.mystreetplaces.di.modules.data

import com.alkurop.mystreetplaces.data.pin.PinCacheImpl
import com.alkurop.mystreetplaces.data.pin.PinCahe
import com.alkurop.mystreetplaces.data.pin.PinRepo
import com.alkurop.mystreetplaces.data.pin.PinRepoImpl
import com.alkurop.mystreetplaces.db.RealmProvider
import dagger.Module
import dagger.Provides


@Module
open class RepoModule {

    @Provides fun provicePinCache(realmProvider: RealmProvider): PinCahe {
        return PinCacheImpl(realmProvider)
    }

    @Provides fun providePinRepo(pinCahe: PinCahe): PinRepo {
        return PinRepoImpl(pinCahe)
    }

}
