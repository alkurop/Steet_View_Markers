package com.alkurop.mystreetplaces.ui.base

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.alkurop.mystreetplaces.MyStreetPlacesApp
import com.alkurop.mystreetplaces.di.components.ViewComponent
import com.alkurop.mystreetplaces.di.modules.ViewModule

/**
 * Created by alkurop on 7/21/17.
 */
abstract class BaseContainerView @JvmOverloads constructor(context: Context,
                                                           attrs: AttributeSet? = null,
                                                           defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    private var component: ViewComponent? = null

    /** Returns a component instance capable to inject this fragment scoped objects.  */
    protected fun component(): ViewComponent {
        var temp = component
        if (temp == null) {
            val app =  context.applicationContext as MyStreetPlacesApp
            temp = app.component.viewComponent(ViewModule(this))
        }
        component = temp
        return temp
    }


}