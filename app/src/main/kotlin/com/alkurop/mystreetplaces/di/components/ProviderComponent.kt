package com.alkurop.mystreetplaces.di.components

import com.alkurop.mystreetplaces.di.modules.ProviderModule
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(ProviderModule::class))
interface ProviderComponent {
}