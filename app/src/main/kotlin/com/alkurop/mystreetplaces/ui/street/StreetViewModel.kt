package com.alkurop.mystreetplaces.ui.street

import android.support.annotation.StringRes


data class StreetViewModel(val isLoading: Boolean? = null,
                           @StringRes val errorRes:Int? = null)