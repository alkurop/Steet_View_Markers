package com.alkurop.mystreetplaces.ui.pin.view

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.BottomSheetBehavior
import android.view.View
import android.support.design.widget.CoordinatorLayout
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.ui.base.BaseMvpActivity
import kotlinx.android.synthetic.main.fragment_view_pin.view.*

class PinFragment : BottomSheetDialogFragment() {
    companion object {
        val PIN_ID_KEY = "pin_id"
    }

    lateinit var pinStartModel: PinViewStartModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pinStartModel = arguments.getParcelable(PIN_ID_KEY)
    }

    private val mBottomSheetBehaviorCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss()
            }
        }
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val contentView = View.inflate(context, R.layout.fragment_view_pin, null)
        dialog.setContentView(contentView)

        val params = (contentView.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior

        if (behavior != null && behavior is BottomSheetBehavior<*>) {
            behavior.setBottomSheetCallback(mBottomSheetBehaviorCallback)
        }
        contentView.viewPin.setStartModel(pinStartModel)
        contentView.viewPin.navigator = {
            val act = activity as BaseMvpActivity<*>
            act.navigate(it)
        }

    }


}
