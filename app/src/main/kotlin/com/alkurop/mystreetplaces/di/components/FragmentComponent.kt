package com.alkurop.mystreetplaces.di.components

import com.alkurop.mystreetplaces.di.annotations.PerFragment
import com.alkurop.mystreetplaces.di.modules.FragmentModule
import dagger.Subcomponent


@PerFragment
@Subcomponent(modules = arrayOf(FragmentModule::class))
interface FragmentComponent {


}
