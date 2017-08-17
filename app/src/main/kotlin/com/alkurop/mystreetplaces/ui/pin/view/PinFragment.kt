package com.alkurop.mystreetplaces.ui.pin.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.CoordinatorLayout
import android.view.View
import android.view.WindowManager
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.ui.base.BaseMvpActivity
import com.alkurop.mystreetplaces.ui.pin.picture.container.PictureActivity
import kotlinx.android.synthetic.main.fragment_view_pin.*
import kotlinx.android.synthetic.main.fragment_view_pin.view.*

class PinFragment : BottomSheetDialogFragment() {
    companion object {
        val CONFIG = "pin_id"
        val handler = Handler()
    }

    private lateinit var pinStartModel: PinViewStartModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pinStartModel = arguments.getParcelable(CONFIG)
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
        dialog.window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if ((requestCode == PinViewPresenterImpl.REQUEST_EDIT_CODE
                || requestCode == PictureActivity.REQUEST_CODE)
                && resultCode == Activity.RESULT_OK) {
            handler.postDelayed({
                if (view != null) {
                    val pinView = view?.findViewById(R.id.viewPin) as PinView
                    pinView.reload()
                } else
                    dismiss()
            }, 200)
        }
    }


}
