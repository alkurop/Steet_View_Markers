package com.alkurop.mystreetplaces.di.modules.ui

import android.view.View
import com.alkurop.mystreetplaces.di.annotations.PerView
import dagger.Module
import dagger.Provides

/**
 * Created by alkurop on 7/21/17.
 */
@Module
class ViewModule(private val view: View) {

    @Provides @PerView internal fun fragment(): View {
        return view
    }
}