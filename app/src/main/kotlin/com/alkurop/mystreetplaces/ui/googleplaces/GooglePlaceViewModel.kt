package com.alkurop.mystreetplaces.ui.googleplaces

import android.graphics.Bitmap
import com.alkurop.mystreetplaces.data.search.GooglePlace

data class GooglePlaceViewModel(val googlePlace: GooglePlace? = null,
                                val pictures: List<Bitmap>? = null)