package com.alkurop.mystreetplaces.di.components

import com.alkurop.mystreetplaces.MyStreetPlacesApp
import com.alkurop.mystreetplaces.di.modules.ActivityModule
import com.alkurop.mystreetplaces.di.modules.ApiModule
import com.alkurop.mystreetplaces.di.modules.ApplicationModule
import com.alkurop.mystreetplaces.di.modules.BroadcastReceiverModule
import com.alkurop.mystreetplaces.di.modules.CommandModule
import com.alkurop.mystreetplaces.di.modules.FragmentModule
import com.alkurop.mystreetplaces.di.modules.PresenterModule
import com.alkurop.mystreetplaces.di.modules.RepoModule
import com.alkurop.mystreetplaces.di.modules.ServiceModule
import com.alkurop.mystreetplaces.di.modules.UtilityModule
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

  fun viewComponent(): ViewComponent

}
