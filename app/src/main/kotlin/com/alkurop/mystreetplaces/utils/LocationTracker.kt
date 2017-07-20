package com.alkurop.mystreetplaces.utils

import android.content.Intent
import android.support.v4.app.FragmentActivity


interface LocationTracker : MapLocationSource {

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

    fun setUp(activity: FragmentActivity, onFailedListener: () -> Unit)

}