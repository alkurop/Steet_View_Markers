package com.alkurop.mystreetplaces.di.components

import com.alkurop.mystreetplaces.di.modules.BroadcastReceiverModule
import dagger.Subcomponent


@Subcomponent(modules = arrayOf(BroadcastReceiverModule::class))
interface BroadcastReceiverComponent {


}

