package com.alkurop.mystreetplaces.di.components

import com.alkurop.mystreetplaces.di.modules.ui.BroadcastReceiverModule
import dagger.Subcomponent


@Subcomponent(modules = arrayOf(BroadcastReceiverModule::class))
interface BroadcastReceiverComponent {


}

