package com.alkurop.mystreetplaces.ui.pin.view

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.widget.TextView
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.ui.base.BaseMvpView
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
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
    lateinit var id: String

    init {
        inflate(context, R.layout.view_pin, this)
    }

    @Inject lateinit var presenter: PinViewPresenter

    override fun onAttachedToWindow() {
        component().inject(this)
        super.onAttachedToWindow()
        locationView = findViewById(R.id.location) as TextView
        titleView = findViewById(R.id.title) as TextView
        descritionView = findViewById(R.id.description) as TextView
        findViewById(R.id.editButton).setOnClickListener { presenter.onEdit() }
        presenter.loadPinDetails(id)
    }

    override fun getSubject(): Observable<PinViewModel> = presenter.viewBus

    override fun getNavigation(): Observable<NavigationAction> = presenter.navBus

    override fun unsubscribe() {
        presenter.unsubscribe()
    }

    fun setPinId(id: String) {
        this.id = id
    }

    override fun onSaveInstanceState(): Parcelable {
        val onSaveInstanceState = super.onSaveInstanceState()
        val bundle = Bundle()
        bundle.putParcelable("state", onSaveInstanceState)
        bundle.putString("id", id)
        return onSaveInstanceState
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
        }
    }
}