package com.alkurop.mystreetplaces.di.components

import com.alkurop.mystreetplaces.di.annotations.PerActivity
import com.alkurop.mystreetplaces.di.modules.ActivityModule
import dagger.Subcomponent

/** Injects activities.  */
@PerActivity
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {

}

