package com.alkurop.mystreetplaces.ui.places

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.data.search.GooglePlacesSearchImpl
import com.alkurop.mystreetplaces.ui.base.BaseMvpView
import javax.inject.Inject

class GooglePlaceView @JvmOverloads constructor(context: Context,
                                                attrs: AttributeSet? = null,
                                                defStyleAttr: Int = 0)
    : BaseMvpView<GooglePlaceViewModel>(context, attrs, defStyleAttr) {

    companion object {
        val KEY_STATE = "STATE"
        val KEY_MODEL = "MODEL"
    }

    lateinit var locationView: TextView
    lateinit var titleView: TextView
    lateinit var addressView: TextView
    lateinit var addressTitle: TextView
    lateinit var recyclerView: RecyclerView

    @Inject lateinit var presenter: GooglePlaceViewPresenter
    private lateinit var mPlace: GooglePlaceViewViewStartModel

    init {
        inflate(context, R.layout.view_google_place, this)
    }

    override fun getSubject() = presenter.viewBus

    override fun getNavigation() = presenter.navBus

    override fun onAttachedToWindow() {
        component().inject(this)
        super.onAttachedToWindow()
        locationView = findViewById(R.id.location_view)
        titleView = findViewById(R.id.title)
        addressView = findViewById(R.id.address)
        addressTitle = findViewById(R.id.address_title)
        recyclerView = findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.visibility = View.GONE
        val activity = context as Activity
        presenter.googleSearch = GooglePlacesSearchImpl(activity)
    }

    override fun onSaveInstanceState(): Parcelable {
        val onSaveInstanceState = super.onSaveInstanceState()
        val bundle = Bundle()
        bundle.putParcelable(KEY_STATE, onSaveInstanceState)
        bundle.putParcelable(KEY_MODEL, mPlace)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val bundle = state as Bundle
        mPlace = bundle.getParcelable(KEY_MODEL)
        super.onRestoreInstanceState(bundle.getParcelable(KEY_STATE))
        presenter.onStart(mPlace.place)
    }

    override fun renderView(viewModel: GooglePlaceViewModel) {
        with(viewModel.googlePlace) {
            titleView.text = name
            addressView.text = address
        }
    }

    override fun unsubscribe() {
        presenter.unsubscribe()
    }

    fun setStartModel(googlePlace: GooglePlaceViewViewStartModel) {
        mPlace = googlePlace
        presenter.onStart(mPlace.place)
    }
}