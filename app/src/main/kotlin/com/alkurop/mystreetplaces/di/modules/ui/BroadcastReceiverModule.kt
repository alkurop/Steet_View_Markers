package com.alkurop.mystreetplaces.di.modules.ui

import android.content.BroadcastReceiver
import com.alkurop.mystreetplaces.di.annotations.PerActivity
import dagger.Module
import dagger.Provides


@Module
open class BroadcastReceiverModule(val receiver: BroadcastReceiver) {

  @Provides
  @PerActivity
  internal fun receiver(): BroadcastReceiver {
    return receiver
  }

}
