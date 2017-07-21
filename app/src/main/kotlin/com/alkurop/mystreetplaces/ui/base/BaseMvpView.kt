package com.alkurop.mystreetplaces.ui.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import com.alkurop.mystreetplaces.ui.navigation.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber


abstract class BaseMvpView<T> @JvmOverloads constructor(context: Context,
                                                        attrs: AttributeSet? = null,
                                                        defStyleAttr: Int = 0)
    : BaseContainerView(context, attrs, defStyleAttr) {
    lateinit var subscriptions: CompositeDisposable

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
        if (it is ActivityNavigationAction) {
            navigateActivity(it)
        } else if (it is FragmentNavigationAction) {
            navigateFragment(it)
        } else if (it == NoArgsNavigation.BACK_ACTION) {
            onBackward()
        } else if (it == NoArgsNavigation.FORWARD_ACTION) {
            onForward()
        } else if (it is UriNavigationAction) {
            navigateUri(it)
        }
    }

    open fun navigateActivity(action: ActivityNavigationAction) {
        val intent = Intent(context, action.endpoint)
        intent.putExtra(BaseMvpActivity.ARGS_KEY, action.args)
        if (action.startForResult) {
            startForResult(intent)
        } else {
            context.startActivity(intent)
            if (action.isShouldFinish) {
                (context as Activity).finish()
            }
        }
    }

    open fun startForResult(intent: Intent) {
        throw NotImplementedError()
    }

    open fun navigateFragment(action: FragmentNavigationAction) {
        throw NotImplementedError()
    }

    open fun navigateUri(action: UriNavigationAction) {
        context.startActivity(action.intent)
    }

    abstract fun renderView(viewModel: T)

    open fun onForward() {
        throw NotImplementedError()
    }

    open fun onBackward() {
        (context as Activity).onBackPressed()
    }
}