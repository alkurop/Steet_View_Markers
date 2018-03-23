package com.alkurop.mystreetplaces.ui.base

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.View
import com.alkurop.mystreetplaces.ui.BaseFragment
import com.alkurop.mystreetplaces.ui.navigation.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

abstract class BaseMvpFragment<T> : BaseFragment() {
    lateinit var subscriptions: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscriptions = CompositeDisposable()
    }

    abstract fun getSubject(): Observable<T>
    abstract fun getNavigation(): Observable<NavigationAction>
    abstract fun unsubscribe()
    abstract fun renderView(viewModel: T)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sub = getSubject()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ renderView(it) }, { Timber.e(it) })
        val sub2 = getNavigation()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ navigate(it) }, { Timber.e(it) })
        subscriptions.add(sub)
        subscriptions.add(sub2)
    }

    open fun navigate(it: NavigationAction) {
        if (it is ActivityNavigationAction) {
            navigateActivity(it)
        } else if (it is FragmentNavigationAction) {
            navigateFragment(it)
        } else if (it == NoArgsNavigation.BACK_ACTION) {
            isAdded
                .takeIf { it }
                .let { onBackward() }
        } else if (it is UriNavigationAction) {
            navigateUri(it)
        } else if (it == NoArgsNavigation.FORWARD_ACTION) {
            onForward()
        } else if (it is BottomsheetFragmentNavigationAction) {
            navigateBottomsheet(it)
        }
    }

    fun navigateBottomsheet(action: BottomsheetFragmentNavigationAction) {
        val bottomSheetDialogFragment = action.endpoint.newInstance()
        bottomSheetDialogFragment.arguments = action.args
        bottomSheetDialogFragment.show(activity!!.supportFragmentManager, bottomSheetDialogFragment.tag)
    }

    open fun onBackward() {
        activity?.onBackPressed()
    }

    open fun onForward() {
        throw NotImplementedError()
    }

    open fun navigateFragment(action: FragmentNavigationAction) {
        val tag = action.endpoint.canonicalName
        val hasFragmentInBackStack = hasFragmentInBackStack(tag)
        if (hasFragmentInBackStack && action.args != null) {
            childFragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            addFragment(action)
        } else if (hasFragmentInBackStack) {
            childFragmentManager.popBackStack(tag, 0)
        } else {
            addFragment(action)
        }
    }

    private fun addFragment(action: FragmentNavigationAction) {
        val tag = action.endpoint.canonicalName
        val fragment = action.endpoint.newInstance()
        fragment.arguments = action.args
        val transaction = childFragmentManager.beginTransaction()
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
        val backStackCount = childFragmentManager.backStackEntryCount
        (0 until backStackCount)
            .forEach {
                val name = childFragmentManager.getBackStackEntryAt(it).name
                if (name == tag) {
                    return true
                }
            }
        return false
    }

    open fun navigateActivity(action: ActivityNavigationAction) {
        val intent = Intent(activity, action.endpoint)
        intent.putExtra(BaseMvpActivity.ARGS_KEY, action.args)
        if (action.startForResult) {
            startForResult(intent, action.requestCode)
        } else {
            startActivity(intent)
            if (action.isShouldFinish) {
                activity?.finish()
            }
        }
        val overrideAnimation = action.overrideAnimation
        if (overrideAnimation != null) {
            activity?.overridePendingTransition(overrideAnimation[0], overrideAnimation[1])
        }
    }

    open fun startForResult(intent: Intent, code: Int) {
        startActivityForResult(intent, code)
    }

    open fun navigateUri(action: UriNavigationAction) {
        startActivity(action.intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        subscriptions.clear()
        unsubscribe()
    }
}

