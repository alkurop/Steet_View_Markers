package com.alkurop.mystreetplaces.ui.pin.placedetails

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import com.alkurop.mystreetplaces.data.search.GooglePlace
import com.google.android.gms.location.places.Place

class PinDetailsFragment : BottomSheetDialogFragment() {
    companion object {
        val KEY_PALCE = "key_place"
    }

    private lateinit var place: GooglePlace
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}