package com.alkurop.mystreetplaces.ui.base

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.View
import com.alkurop.mystreetplaces.ui.BaseFragment
import com.alkurop.mystreetplaces.ui.navigation.ActivityNavigationAction
import com.alkurop.mystreetplaces.ui.navigation.FragmentNavigationAction
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.alkurop.mystreetplaces.ui.navigation.NoArgsNavigation
import com.alkurop.mystreetplaces.ui.navigation.UriNavigationAction
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

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
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


  fun navigate(it: NavigationAction) {
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
    }
  }

  open fun onBackward() {
    activity.onBackPressed()
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
    (0..backStackCount - 1)
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
      startForResult(intent)
    } else {
      startActivity(intent)
      if (action.isShouldFinish) {
        activity.finish()
      }
    }
  }


  open fun startForResult(intent: Intent) {
    throw NotImplementedError()
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

