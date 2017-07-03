package com.alkurop.mystreetplaces.di.components

import com.alkurop.mystreetplaces.di.modules.ServiceModule
import dagger.Subcomponent


@Subcomponent(modules = arrayOf(ServiceModule::class))
interface ServiceComponent {


}

