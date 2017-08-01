package com.alkurop.mystreetplaces.ui.street

import android.support.annotation.StringRes
import com.alkurop.mystreetplaces.data.pin.PinPlace

data class StreetViewModel(val isLoading: Boolean? = null,
                           @StringRes val errorRes: Int? = null,
                           val places: List<PinPlace>? = null)