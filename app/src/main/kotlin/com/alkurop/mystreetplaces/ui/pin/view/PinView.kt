package com.alkurop.mystreetplaces.ui.pin.view

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.ui.base.BaseMvpView
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.alkurop.mystreetplaces.ui.pin.pictures.PicturesAdapter
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by alkurop on 7/21/17.
 */
class PinView @JvmOverloads constructor(context: Context,
                                        attrs: AttributeSet? = null,
                                        defStyleAttr: Int = 0)
    : BaseMvpView<PinViewModel>(context, attrs, defStyleAttr) {
    lateinit var locationView: TextView
    lateinit var titleView: TextView
    lateinit var descritionView: TextView
    lateinit var descritionTitle: TextView
    lateinit var addressView: TextView
    lateinit var addressTitle: TextView
    lateinit var recyclerView: RecyclerView
    lateinit var id: String

    init {
        inflate(context, R.layout.view_pin, this)
    }

    @Inject lateinit var presenter: PinViewPresenter

    override fun onAttachedToWindow() {
        component().inject(this)
        super.onAttachedToWindow()
        locationView = findViewById(R.id.location_view)
        titleView = findViewById(R.id.title)
        descritionTitle = findViewById(R.id.description_title)
        descritionView = findViewById(R.id.description)
        addressView = findViewById(R.id.address)
        addressTitle = findViewById(R.id.address_title)
        recyclerView = findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        val picturesAdapter = PicturesAdapter()
        picturesAdapter.onPictureClick = { position -> presenter.onPictureClick(picturesAdapter.getItems(), position) }
        recyclerView.adapter = picturesAdapter
        findViewById<View>(R.id.editBtn).setOnClickListener { presenter.onEdit() }
        findViewById<View>(R.id.navigateBtn).setOnClickListener { presenter.onNavigate() }
        findViewById<View>(R.id.shareBtn).setOnClickListener { presenter.onShare() }
        presenter.loadPinDetails(id)
    }

    override fun getSubject(): Observable<PinViewModel> = presenter.viewBus

    override fun getNavigation(): Observable<NavigationAction> = presenter.navBus

    override fun unsubscribe() {
        presenter.unsubscribe()
    }

    fun setStartModel(model: PinViewStartModel) {
        this.id = model.pinId
    }

    override fun onSaveInstanceState(): Parcelable {
        val onSaveInstanceState = super.onSaveInstanceState()
        val bundle = Bundle()
        bundle.putParcelable("state", onSaveInstanceState)
        bundle.putString("id", id)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val bundle = state as Bundle
        id = bundle.getString("id")
        super.onRestoreInstanceState(bundle.getParcelable("state"))
    }

    override fun renderView(viewModel: PinViewModel) {
        with(viewModel.pinDto) {
            titleView.text = title
            descritionView.text = description
            locationView.text = "$lat     $lon"
            (recyclerView.adapter as PicturesAdapter).setItems(pictures)
            descritionTitle.visibility = if (description.isNullOrEmpty()) View.GONE else View.VISIBLE
            descritionView.visibility = if (description.isNullOrEmpty()) View.GONE else View.VISIBLE
            addressView.visibility = if (address == null) View.GONE else View.VISIBLE
            addressTitle.visibility = if (address == null) View.GONE else View.VISIBLE
            addressView.text = address
        }
    }

    fun reload() {
        presenter.loadPinDetails(id)
    }
}