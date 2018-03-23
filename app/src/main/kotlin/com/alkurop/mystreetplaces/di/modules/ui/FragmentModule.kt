package com.alkurop.mystreetplaces.di.modules.ui

import android.app.Activity
import android.support.v4.app.Fragment
import com.alkurop.mystreetplaces.di.annotations.PerFragment
import dagger.Module
import dagger.Provides


@Module
class FragmentModule(private val fragment: Fragment) {

  @Provides @PerFragment internal fun fragment(): Fragment {
    return fragment
  }

  @Provides @PerFragment internal fun activity(): Activity {
    return fragment.activity as Activity
  }

}
