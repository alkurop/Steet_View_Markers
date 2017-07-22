package com.alkurop.mystreetplaces.ui.pin.bottomsheet

import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.BottomSheetBehavior
import android.view.View


class PinFragment : BottomSheetDialogFragment() {
    private val mBottomSheetBehaviorCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss()
            }
        }
    }
}
