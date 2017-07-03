package com.alkurop.mystreetplaces.ui.navigation

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment

data class FragmentNavigationAction(val endpoint: Class<out Fragment>,
                                    @IdRes val containerId: Int,
                                    val args: Bundle? = null,
                                    val isShouldSaveToBackStack: Boolean = true,
                                    val isShouldReplace: Boolean = true

) : NavigationAction {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other?.javaClass != javaClass) return false

    other as FragmentNavigationAction

    if (endpoint != other.endpoint) return false
    if (containerId != other.containerId) return false
    if (!args.equalBundles(other.args)) return false
    if (isShouldSaveToBackStack != other.isShouldSaveToBackStack) return false
    if (isShouldReplace != other.isShouldReplace) return false

    return true
  }

  override fun hashCode(): Int {
    var result = endpoint.hashCode()
    result = 31 * result + containerId
    result = 31 * result + (args?.hashCode() ?: 0)
    result = 31 * result + isShouldSaveToBackStack.hashCode()
    result = 31 * result + isShouldReplace.hashCode()
    return result
  }
}
