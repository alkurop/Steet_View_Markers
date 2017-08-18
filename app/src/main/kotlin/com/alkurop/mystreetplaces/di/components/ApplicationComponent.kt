package com.alkurop.mystreetplaces.di.components

import com.alkurop.mystreetplaces.MyStreetPlacesApp
import com.alkurop.mystreetplaces.di.modules.*
import com.alkurop.mystreetplaces.di.modules.data.ApiModule
import com.alkurop.mystreetplaces.di.modules.data.CommandModule
import com.alkurop.mystreetplaces.di.modules.data.RepoModule
import com.alkurop.mystreetplaces.di.modules.ui.*
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(
        ApplicationModule::class,
        ApiModule::class,
        PresenterModule::class,
        RepoModule::class,
        CommandModule::class,
        UtilityModule::class))
interface ApplicationComponent {

    fun inject(application: MyStreetPlacesApp)

    fun activityComponent(module: ActivityModule): ActivityComponent

    fun fragmentComponent(module: FragmentModule): FragmentComponent

    fun receiverComponent(module: BroadcastReceiverModule): BroadcastReceiverComponent

    fun serviceComponent(module: ServiceModule): ServiceComponent

    fun viewComponent(module: ViewModule): ViewComponent

    fun providerComponent(module: ProviderModule): ProviderComponent

}
