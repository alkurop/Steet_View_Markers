package com.alkurop.mystreetplaces.ui.search

import com.alkurop.mystreetplaces.data.pin.PinRepo
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
import io.reactivex.functions.Function
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
        val searchObservable = pinRepo.search(query).toObservable().share()
        val bounds = LocationUtils.getBounds(location, 10000)
        val filter = searchObservable.filter { it.size < 10 }
        val searchObservableWithGoogle = filter
                .switchMap { localPredictions ->
                    googlePlacesSearch
                            .getPlaces(query, bounds)
                            .toObservable()
                            .map { googlePredictions ->
                                val list = mutableListOf<Any>()
                                list.addAll(localPredictions)
                                list.addAll(googlePredictions)
                                list.take(10)
                            }
                            .doOnError { Timber.e(it) }
                            .onErrorResumeNext(Observable.just(localPredictions))

                }
        val searchObservableWithoutGoogle = searchObservable.filter { it.size >= 10 }

        val dis = Observable.merge(searchObservableWithGoogle, searchObservableWithoutGoogle)
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
        appDataBus.pinSearch.onNext(AppDataBus.PinSearchModel(pinDto, if (query.isNullOrBlank()) pinDto.title else query))
    }

    override fun onPredictionClicked(prediction: AutocompletePrediction) {
        appDataBus.googlePlacesSearch.onNext(AppDataBus.GooglePlaceSearchModel(prediction.placeId!!, query))
    }
}