package com.alkurop.mystreetplaces.ui.navigation

import android.app.Activity
import android.os.Bundle
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.alkurop.mystreetplaces.ui.navigation.equalBundles

data class ActivityNavigationAction(val endpoint: Class<out Activity>,
                                    val args: Bundle? = null,
                                    val isShouldFinish: Boolean = false,
                                    val startForResult: Boolean = false,
                                    val requestCode: Int = 0,
                                    val overrideAnimation: IntArray? = null) : NavigationAction {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as ActivityNavigationAction

        if (endpoint != other.endpoint) return false
        if (!args.equalBundles(other.args)) return false
        if (isShouldFinish != other.isShouldFinish) return false

        return true
    }

    override fun hashCode(): Int {
        var result = endpoint.hashCode()
        result = 31 * result + (args?.hashCode() ?: 0)
        result = 31 * result + isShouldFinish.hashCode()
        return result
    }

}
