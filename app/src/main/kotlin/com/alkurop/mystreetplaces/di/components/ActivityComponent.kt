package com.alkurop.mystreetplaces.di.components

import com.alkurop.mystreetplaces.di.annotations.PerActivity
import com.alkurop.mystreetplaces.di.modules.ui.ActivityModule
import com.alkurop.mystreetplaces.ui.home.MainActivity
import com.alkurop.mystreetplaces.ui.pin.activity.DropPinActivity
import com.alkurop.mystreetplaces.ui.street.StreetActivity
import dagger.Subcomponent

/** Injects activities.  */
@PerActivity
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {
    fun inject(activity: MainActivity)

    fun inject(activity: StreetActivity)

    fun inject(pinActivity: DropPinActivity)
}

