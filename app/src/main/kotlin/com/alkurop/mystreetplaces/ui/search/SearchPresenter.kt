package com.alkurop.mystreetplaces.ui.search

import com.alkurop.mystreetplaces.domain.pin.PinDto
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import io.reactivex.subjects.Subject

interface SearchPresenter {
    val viewBus: Subject<SearchViewModel>
    val navBus: Subject<NavigationAction>

    fun onSearchQuerySubmit(query: String)

    fun unsubscribe()

    fun onSearchItemSelected(pinDto: PinDto)
}