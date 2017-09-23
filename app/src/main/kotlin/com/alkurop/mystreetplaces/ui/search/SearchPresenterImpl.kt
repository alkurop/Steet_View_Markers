package com.alkurop.mystreetplaces.ui.search

import com.alkurop.mystreetplaces.data.pin.PinRepo
import com.alkurop.mystreetplaces.domain.pin.PinDto
import com.alkurop.mystreetplaces.intercom.SearchBus
import com.alkurop.mystreetplaces.ui.createNavigationSubject
import com.alkurop.mystreetplaces.ui.createViewSubject
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.Subject
import timber.log.Timber

class SearchPresenterImpl(val pinRepo: PinRepo, val searchBus: SearchBus) : SearchPresenter {
    override val viewBus: Subject<SearchViewModel> = createViewSubject()
    override val navBus: Subject<NavigationAction> = createNavigationSubject()
    val compositeSubscription = CompositeDisposable()
    var query = ""
    override fun onSearchQuerySubmit(query: String) {
        this.query = query
        val dis = pinRepo
                .search(query)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    val viewModel = SearchViewModel(it)
                    viewBus.onNext(viewModel)
                }, { Timber.e(it) })
        compositeSubscription.add(dis)
    }

    override fun unsubscribe() {
        compositeSubscription.clear()
    }

    override fun onSearchItemSelected(pinDto: PinDto) {
        searchBus.pinSearch.onNext(SearchBus.SearchModel(pinDto, if (query.isNullOrBlank()) pinDto.title else query))
    }
}