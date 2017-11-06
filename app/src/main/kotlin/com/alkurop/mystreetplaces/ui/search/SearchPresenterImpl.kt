package com.alkurop.mystreetplaces.ui.search

import com.alkurop.mystreetplaces.data.pin.PinRepo
import com.alkurop.mystreetplaces.data.search.GooglePlace
import com.alkurop.mystreetplaces.data.search.GooglePlacesSearch
import com.alkurop.mystreetplaces.domain.pin.PinDto
import com.alkurop.mystreetplaces.intercom.AppDataBus
import com.alkurop.mystreetplaces.ui.createNavigationSubject
import com.alkurop.mystreetplaces.ui.createViewSubject
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.alkurop.mystreetplaces.utils.LocationUtils
import com.google.android.gms.location.places.AutocompletePrediction
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.Subject
import timber.log.Timber

class SearchPresenterImpl(val pinRepo: PinRepo, val appDataBus: AppDataBus) : SearchPresenter {
    override val viewBus: Subject<SearchViewModel> = createViewSubject()
    override val navBus: Subject<NavigationAction> = createNavigationSubject()
    override lateinit var googlePlacesSearch: GooglePlacesSearch
    val compositeSubscription = CompositeDisposable()
    var query = ""

    override fun onSearchQuerySubmit(query: String, location: LatLng) {
        this.query = query
        val searchLocal = pinRepo.search(query).toObservable()
        val bounds = LocationUtils.getBounds(location, 10000)
        val searchGoogle =
                googlePlacesSearch
                        .getPlaces(query, bounds)
                        .toObservable()
                        .onErrorResumeNext(Observable.empty())

        val compositeObservable = Observable.combineLatest(searchGoogle, searchLocal, BiFunction<List<Any>, List<Any>, List<Any>> { var1, var2 ->
            val resLust = mutableListOf<Any>()
            resLust.addAll(var2)
            resLust.addAll(var1)
            return@BiFunction resLust

        })

        val dis = compositeObservable
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
        appDataBus.pinSearch.onNext(AppDataBus.PinSearchModel(pinDto, if (query.isBlank()) pinDto.title else query))
    }

    override fun onPlaceClicked(place: GooglePlace) {
        appDataBus.googlePlacesSearch.onNext(AppDataBus.GooglePlaceSearchModel(place, query))
    }
}