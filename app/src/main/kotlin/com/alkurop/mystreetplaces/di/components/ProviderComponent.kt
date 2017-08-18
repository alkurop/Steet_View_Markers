package com.alkurop.mystreetplaces.di.components

import com.alkurop.mystreetplaces.di.modules.ProviderModule
import com.alkurop.mystreetplaces.ui.home.SearchContentProvider
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(ProviderModule::class))
interface ProviderComponent {
    fun inject(searchContentProvider: SearchContentProvider)
}