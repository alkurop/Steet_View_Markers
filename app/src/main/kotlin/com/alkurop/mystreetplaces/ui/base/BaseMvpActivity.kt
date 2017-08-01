package com.alkurop.mystreetplaces.ui.base

import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.FragmentManager
import android.widget.Toast
import com.alkurop.mystreetplaces.ui.navigation.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

abstract class BaseMvpActivity<T> : BaseActivity() {

    companion object {
        val ARGS_KEY = "args"
    }

    lateinit var subscriptions: CompositeDisposable
    var isRestoredState = false

    abstract fun getSubject(): Observable<T>

    abstract fun getNavigation(): Observable<NavigationAction>

    abstract fun unsubscribe()

    abstract fun renderView(viewModel: T)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isRestoredState = savedInstanceState != null
        subscriptions = CompositeDisposable()
    }

    override fun setupRootView(layoutId: Int) {
        super.setupRootView(layoutId)
        subscribeToPresenter()
    }

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

    override fun onSaveInstanceState(outState: Bundle?) {
        val bundle = outState ?: Bundle()
        super.onSaveInstanceState(bundle)
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
        } else if (it is BottomsheetFragmentNavigationAction) {
            navigateBottomsheet(it)
        }
    }

    open fun onForward() {
        throw NotImplementedError()
    }

    open fun onBackward() {
        onBackPressed()
    }

    open fun navigateFragment(action: FragmentNavigationAction) {
        val tag = action.endpoint.canonicalName
        val hasFragmentInBackStack = hasFragmentInBackStack(tag)

        if (hasFragmentInBackStack && action.args != null) {
            supportFragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            addFragment(action)
        } else if (hasFragmentInBackStack) {
            supportFragmentManager.popBackStack(tag, 0)
        } else {
            addFragment(action)
        }
    }

    private fun addFragment(action: FragmentNavigationAction) {
        val tag = action.endpoint.canonicalName
        val fragment = action.endpoint.newInstance()
        fragment.arguments = action.args
        val transaction = supportFragmentManager.beginTransaction()
        if (action.isShouldReplace) {
            transaction.replace(action.containerId, fragment, tag)
        } else {
            transaction.add(action.containerId, fragment, tag)
        }
        if (action.isShouldSaveToBackStack) {
            transaction.addToBackStack(tag)
        }
        transaction.commit()
    }

    fun hasFragmentInBackStack(tag: String): Boolean {
        val backStackCount = supportFragmentManager.backStackEntryCount
        (0..backStackCount - 1)
                .forEach {
                    val name = supportFragmentManager.getBackStackEntryAt(it).name
                    if (name == tag) {
                        return true
                    }
                }
        return false
    }

    open fun navigateActivity(action: ActivityNavigationAction) {
        val intent = Intent(this, action.endpoint)
        intent.putExtra(ARGS_KEY, action.args)
        if (action.startForResult) {
            startForResult(intent)
        } else {
            startActivity(intent)
            if (action.isShouldFinish) {
                finish()
            }
        }
    }

    open fun startForResult(intent: Intent) {
        throw NotImplementedError()
    }

    open fun navigateUri(action: UriNavigationAction) {
        startActivity(action.intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.clear()
        unsubscribe()
    }

    fun displayError(@StringRes errorMes: Int) {
        Toast.makeText(this, errorMes, Toast.LENGTH_SHORT).show()
    }

    fun navigateBottomsheet(action: BottomsheetFragmentNavigationAction) {
        val bottomSheetDialogFragment = action.endpoint.newInstance()
        bottomSheetDialogFragment.arguments = action.args
        bottomSheetDialogFragment.show(supportFragmentManager, bottomSheetDialogFragment.tag)
    }
}

