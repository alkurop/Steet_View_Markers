package com.alkurop.mystreetplaces.ui.pin.placedetails

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.CoordinatorLayout
import android.view.View
import android.view.WindowManager
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.ui.base.BaseMvpActivity
import com.alkurop.mystreetplaces.ui.navigation.ActivityNavigationAction
import com.alkurop.mystreetplaces.ui.pin.drop.DropPinActivity
import com.alkurop.mystreetplaces.ui.pin.picture.container.PictureActivity
import com.alkurop.mystreetplaces.ui.pin.view.PinFragment
import com.alkurop.mystreetplaces.ui.pin.view.PinViewPresenterImpl
import com.alkurop.mystreetplaces.ui.places.GooglePlaceViewViewStartModel
import kotlinx.android.synthetic.main.fragment_place_details.view.*

class PinDetailsFragment : BottomSheetDialogFragment() {
    companion object {
        val KEY_PALCE = "key_place"
    }

    private lateinit var placeStartModel: GooglePlaceViewViewStartModel


    private val mBottomSheetBehaviorCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        placeStartModel = arguments.getParcelable(KEY_PALCE)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val contentView = View.inflate(context, R.layout.fragment_place_details, null)
        dialog.setContentView(contentView)
        dialog.window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        val params = (contentView.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior

        if (behavior != null && behavior is BottomSheetBehavior<*>) {
            behavior.setBottomSheetCallback(mBottomSheetBehaviorCallback)
        }
        val googlePlaceView = contentView.googlePlaceView
        googlePlaceView.setStartModel(placeStartModel)
        googlePlaceView.navigator = {
            val act = activity as BaseMvpActivity<*>
            act.navigate(it)

            if ((it is ActivityNavigationAction) && it.endpoint.canonicalName == DropPinActivity::class.java.canonicalName) {
                dismiss()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if ((requestCode == PinViewPresenterImpl.REQUEST_EDIT_CODE
                || requestCode == PictureActivity.REQUEST_CODE)
                && resultCode == Activity.RESULT_OK) {
            PinFragment.handler.postDelayed({ dismiss() }, 200)
        }
    }
}