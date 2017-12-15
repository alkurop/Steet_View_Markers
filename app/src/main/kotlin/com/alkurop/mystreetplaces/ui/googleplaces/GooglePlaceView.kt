package com.alkurop.mystreetplaces.ui.googleplaces

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.util.Linkify
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.data.pin.PictureWrapper
import com.alkurop.mystreetplaces.data.search.GooglePlacesSearchImpl
import com.alkurop.mystreetplaces.ui.base.BaseMvpView
import com.alkurop.mystreetplaces.ui.pin.pictures.PicturesAdapter
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import timber.log.Timber
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
    lateinit var descriptionView: TextView
    lateinit var descriptionTitleView: TextView
    lateinit var recyclerView: RecyclerView

    @Inject lateinit var presenter: GooglePlaceViewPresenter
    private lateinit var mPlace: GooglePlaceViewViewStartModel
    val picturesAdapter by lazy { PicturesAdapter() }

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
        descriptionView = findViewById(R.id.description)
        descriptionTitleView = findViewById(R.id.description_title)
        recyclerView = findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = picturesAdapter
        val activity = context as Activity
        presenter.googleSearch = GooglePlacesSearchImpl(activity)
        presenter.googleApiClient = GoogleApiClient.Builder(activity)
                .enableAutoManage(activity as FragmentActivity, 1, {})
                .addApi(Places.GEO_DATA_API)
                .build()
        picturesAdapter.onPictureClick = { presenter.onPictureClick(picturesAdapter.getItems(), it) }
        presenter.onStart(mPlace.place)

        findViewById<View>(R.id.saveButton).setOnClickListener { presenter.onSave() }
        findViewById<View>(R.id.shareBtn).setOnClickListener { presenter.onShare() }
        findViewById<View>(R.id.navigateBtn).setOnClickListener { presenter.onNavigate() }
        findViewById<View>(R.id.streetBtn).setOnClickListener { presenter.onStreet() }
    }

    override fun onDetachedFromWindow() {
        presenter.googleApiClient.stopAutoManage(context as FragmentActivity)

        super.onDetachedFromWindow()
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
        viewModel.pin.let { place ->
            with(place) {
                titleView.text = title
                addressView.text = address
                descriptionView.text = description
                val descriptionVisibility = if (description.isNullOrEmpty()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
                descriptionView.visibility = descriptionVisibility
                descriptionTitleView.visibility = descriptionVisibility

                picturesAdapter.setItems(pictures)
                Linkify.addLinks(descriptionView, Linkify.WEB_URLS)

            }
        }
    }

    override fun unsubscribe() {
        presenter.unsubscribe()
    }

    fun setStartModel(googlePlace: GooglePlaceViewViewStartModel) {
        mPlace = googlePlace
    }
}