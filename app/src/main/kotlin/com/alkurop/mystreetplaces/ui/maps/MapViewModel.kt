package com.alkurop.mystreetplaces.ui.maps

import android.support.annotation.StringRes
import com.alkurop.mystreetplaces.data.search.GooglePlace
import com.alkurop.mystreetplaces.domain.pin.PinDto

data class MapViewModel(val isLoading: Boolean? = null,
                        @StringRes val errorRes: Int? = null,
                        val shouldAskForPermission: Boolean = false,
                        val pins: List<PinDto>? = null,
                        val focusMarker: PinDto? = null,
                        val focusPlace: GooglePlace? = null)