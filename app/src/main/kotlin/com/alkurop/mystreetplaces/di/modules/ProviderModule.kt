package com.alkurop.mystreetplaces.di.modules

import android.content.ContentProvider
import com.alkurop.mystreetplaces.di.annotations.PerActivity
import dagger.Module
import dagger.Provides

@Module
open class ProviderModule(val provider: ContentProvider) {

  @Provides
  @PerActivity
  internal fun provider(): ContentProvider {
    return provider
  }

}