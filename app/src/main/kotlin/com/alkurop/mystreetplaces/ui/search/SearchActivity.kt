package com.alkurop.mystreetplaces.ui.search

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.data.search.GooglePlacesSearchImpl
import com.alkurop.mystreetplaces.ui.base.BaseMvpActivity
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLng
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_search.*
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchActivity : BaseMvpActivity<SearchViewModel>() {

    @Inject lateinit var presenter: SearchPresenter
    lateinit var adapter: SearchAdapter

    companion object {
        val KEY_QUERY = "KEY_QUERY"
        val KEY_LOCATION = "location"
    }

    private lateinit var location: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val googlePlacesSearch = GooglePlacesSearchImpl(this)
        component().inject(this)
        setupRootView(R.layout.activity_search)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val googleApiClient = GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, {
                    Timber.e(it.errorMessage)
                })
                .build()
        adapter = SearchAdapter(googlePlacesSearch, googleApiClient)
        adapter.pinClickListener = {
            presenter.onSearchItemSelected(it)
            onBackPressed()
        }
        adapter.googlePlaceClickListener = {
            presenter.onPlaceClicked(it)
            onBackPressed()
        }
        presenter.googlePlacesSearch = googlePlacesSearch
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerDecorator(R.dimen.item_divider))

        RxTextView.afterTextChangeEvents(et_search)
                .debounce(250, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .filter { it.toString().length > 1 }
                .subscribe {
                    val query = it.editable()?.toString() ?: ""
                    presenter.onSearchQuerySubmit(query, location)
                    icClose.visibility = if (query.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE

                }

        icClose.setOnClickListener {
            et_search.text = null
        }

        container.setOnClickListener {
            onBackPressed()
        }
        val query = intent.getBundleExtra(BaseMvpActivity.ARGS_KEY).getString(KEY_QUERY)
        location = intent.getBundleExtra(BaseMvpActivity.ARGS_KEY).getParcelable(KEY_LOCATION)
        et_search.setText(query)
        et_search.setSelection(query.length)

    }

    override fun getSubject(): Observable<SearchViewModel> = presenter.viewBus

    override fun getNavigation(): Observable<NavigationAction> = presenter.navBus

    override fun unsubscribe() {
        presenter.unsubscribe()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun renderView(viewModel: SearchViewModel) {
        adapter.updateItems(viewModel.searchResult)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}