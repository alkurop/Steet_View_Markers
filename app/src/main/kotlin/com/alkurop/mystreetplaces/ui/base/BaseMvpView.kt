package com.alkurop.mystreetplaces.ui.base

import android.content.Context
import android.util.AttributeSet
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

abstract class BaseMvpView<T> @JvmOverloads constructor(context: Context,
                                                        attrs: AttributeSet? = null,
                                                        defStyleAttr: Int = 0)
    : BaseContainerView(context, attrs, defStyleAttr) {
    lateinit var subscriptions: CompositeDisposable
    var navigator : ((NavigationAction) -> Unit)? = null

    abstract fun getSubject(): Observable<T>

    abstract fun getNavigation(): Observable<NavigationAction>

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        subscriptions = CompositeDisposable()
        subscribeToPresenter()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        subscriptions.clear()
        unsubscribe()
    }

    abstract fun unsubscribe()

    private fun subscribeToPresenter() {
        val sub = getSubject()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ renderView(it) }, { Timber.e(it) })
        val sub2 = getNavigation()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ navigate(it) }, { Timber.e(it) })
        subscriptions.add(sub)
        subscriptions.add(sub2)
    }

    private fun navigate(it: NavigationAction) {
      navigator?.invoke(it)
    }

    abstract fun renderView(viewModel: T)

}
