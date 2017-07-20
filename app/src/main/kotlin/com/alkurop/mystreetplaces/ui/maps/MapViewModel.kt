package com.alkurop.mystreetplaces.ui.maps

import android.support.annotation.StringRes


data class MapViewModel(val isLoading: Boolean? = null,
                        @StringRes val errorRes: Int? = null)