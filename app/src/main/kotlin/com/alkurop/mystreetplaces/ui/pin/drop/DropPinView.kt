package com.alkurop.mystreetplaces.ui.pin.drop

import android.content.Context
import android.util.AttributeSet
import com.alkurop.mystreetplaces.ui.base.BaseMvpView
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by alkurop on 7/21/17.
 */
class DropPinView @JvmOverloads constructor(context: Context,
                                            attrs: AttributeSet? = null,
                                            defStyleAttr: Int = 0)
    : BaseMvpView<DropPinViewModel>(context, attrs, defStyleAttr) {

    @Inject lateinit var presenter: DropPinPresenter

    override fun onAttachedToWindow() {
        component().inject(this)
        super.onAttachedToWindow()
    }

    override fun getSubject(): Observable<DropPinViewModel> = presenter.viewBus

    override fun getNavigation(): Observable<NavigationAction> = presenter.navBus

    override fun unsubscribe() {
        presenter.unsubscribe()
    }

    override fun renderView(viewModel: DropPinViewModel) {
    }
}