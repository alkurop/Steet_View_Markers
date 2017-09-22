package com.alkurop.mystreetplaces.ui.home

import android.support.annotation.MenuRes
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import io.reactivex.subjects.Subject

interface MainActivityPresenter {
    val viewBus: Subject<MainActivityView>
    val navBus: Subject<NavigationAction>
    var currentModel: MainActivityView?

    fun start()

    fun unsubscribe()

    fun onFragmentShowed(tag: String)

    fun onDrawerAction(@MenuRes action: Int)

    fun onSearchClicked()
}