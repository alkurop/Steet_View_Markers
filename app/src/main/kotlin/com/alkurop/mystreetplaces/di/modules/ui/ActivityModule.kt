package com.alkurop.mystreetplaces.di.modules.ui

import android.app.Activity
import com.alkurop.mystreetplaces.di.annotations.PerActivity
import dagger.Module
import dagger.Provides


/** Provides activity instance.  */
@Module
open class ActivityModule(val activity: Activity) {

  @Provides
  @PerActivity
  internal fun activity(): Activity {
    return activity
  }

}
