package com.alkurop.mystreetplaces.ui.pin.view

import android.content.Context
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
    lateinit var location: TextView
    lateinit var title: TextView
    lateinit var descrition: TextView


    init {
        inflate(context, R.layout.view_pin, this)
    }

    @Inject lateinit var presenter: PinViewPresenter

    override fun onAttachedToWindow() {
        component().inject(this)
        super.onAttachedToWindow()
        location = findViewById(R.id.location) as TextView
        title = findViewById(R.id.title) as TextView
        descrition = findViewById(R.id.description) as TextView
    }

    override fun getSubject(): Observable<PinViewModel> = presenter.viewBus

    override fun getNavigation(): Observable<NavigationAction> = presenter.navBus

    override fun unsubscribe() {
        presenter.unsubscribe()
    }

    fun onResume() {}
    fun onPause() {}
    fun onDestroy() {}

    override fun renderView(viewModel: PinViewModel) {
    }
}