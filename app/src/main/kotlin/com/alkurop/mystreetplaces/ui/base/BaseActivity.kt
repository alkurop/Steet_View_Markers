package com.alkurop.mystreetplaces.ui.base

import android.content.Intent
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.alkurop.mystreetplaces.MyStreetPlacesApp
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.di.components.ActivityComponent
import com.alkurop.mystreetplaces.di.modules.ui.ActivityModule

abstract class BaseActivity : AppCompatActivity() {

    private var component: ActivityComponent? = null

    private var toolbar: Toolbar? = null

    /** Returns a component instance capable to inject this activity scoped objects.  */
    protected fun component(): ActivityComponent {
        if (component == null) {
            val app = applicationContext as MyStreetPlacesApp
            component = app.component.activityComponent(ActivityModule(this))
        }
        return component!!
    }

    /**
     * Set activity root view and set up tool bar.
     * This method will crash if toolbar is not found in the layout.
     */
    protected open fun setupRootView(@LayoutRes layoutId: Int) {
        setContentView(layoutId)
        toolbar = this.findViewById(R.id.toolbar) as Toolbar
        if (toolbar != null) {
            setSupportActionBar(toolbar)
        }
    }

    /** Return a tool bar that was set up with [.setupRootView].  */
    protected fun getToolbar(): Toolbar {
        if (toolbar == null) {
            throw IllegalStateException("Tool bar is not initialized. Have you called setupRootView?")
        }
        return toolbar!!
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        supportFragmentManager.fragments
                ?.filter { it != null && it.isAdded }
                ?.forEach { fragment ->
                    fragment.onRequestPermissionsResult(requestCode, permissions, grantResults)
                    fragment.childFragmentManager.fragments
                            ?.filter { it != null && it.isAdded }
                            ?.forEach { child ->
                                child.onRequestPermissionsResult(requestCode, permissions, grantResults)
                            }
                }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        supportFragmentManager.fragments
                ?.filter { it != null && it.isAdded }
                ?.forEach { fragment ->
                    fragment.onActivityResult(requestCode, resultCode, data)
                    fragment.childFragmentManager.fragments
                            ?.filter { it != null && it.isAdded }
                            ?.forEach { child ->
                                child.onActivityResult(requestCode, resultCode, data)
                            }
                }
        super.onActivityResult(requestCode, resultCode, data)
    }
}