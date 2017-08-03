package com.alkurop.mystreetplaces.ui.pin.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.widget.TextView
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.ui.base.BaseMvpView
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.alkurop.mystreetplaces.ui.pin.pictures.PicturesAdapter
import com.alkurop.mystreetplaces.utils.CameraPictureHelper
import com.github.alkurop.jpermissionmanager.PermissionOptionalDetails
import com.github.alkurop.jpermissionmanager.PermissionsManager
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by alkurop on 7/21/17.
 */
class PinView @JvmOverloads constructor(context: Context,
                                        attrs: AttributeSet? = null,
                                        defStyleAttr: Int = 0)
    : BaseMvpView<PinViewModel>(context, attrs, defStyleAttr) {
    lateinit var locationView: TextView
    lateinit var titleView: TextView
    lateinit var descritionView: TextView
    lateinit var recyclerView: RecyclerView
    lateinit var id: String

    lateinit var photoHelper: CameraPictureHelper
    var permissionManager: PermissionsManager? = null

    init {
        inflate(context, R.layout.view_pin, this)
    }

    @Inject lateinit var presenter: PinViewPresenter

    override fun onAttachedToWindow() {
        component().inject(this)
        super.onAttachedToWindow()
        locationView = findViewById(R.id.location) as TextView
        titleView = findViewById(R.id.title) as TextView
        descritionView = findViewById(R.id.description) as TextView
        recyclerView = findViewById(R.id.recyclerView) as RecyclerView
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        val picturesAdapter = PicturesAdapter()
        picturesAdapter.onAddPictureClick = {
            presenter.addPicture()
        }
        recyclerView.adapter = picturesAdapter
        findViewById(R.id.editButton).setOnClickListener { presenter.onEdit() }
        presenter.loadPinDetails(id)
    }

    private fun setUpPermissionsManager(function: () -> Unit) {
        if (permissionManager == null) permissionManager = PermissionsManager(context as Activity)
        val permission1 = Pair(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                PermissionOptionalDetails(context.getString(R.string.storage_permission_rationale_title),
                        context.getString(R.string.storage_permission_rationale)))
        val permission2 = Pair(Manifest.permission.CAMERA,
                PermissionOptionalDetails(context.getString(R.string.camera_permission_rationale_title),
                        context.getString(R.string.camera_permission_rationale)))
        permissionManager?.clearPermissions()
        permissionManager?.clearPermissionsListeners()
        permissionManager?.addPermissions(mapOf(permission1, permission2))
        permissionManager?.addPermissionsListener {
            for (pair in it) {
                if (!pair.value)
                    return@addPermissionsListener
            }
            function.invoke()
        }
        permissionManager?.makePermissionRequest()

    }

    override fun getSubject(): Observable<PinViewModel> = presenter.viewBus

    override fun getNavigation(): Observable<NavigationAction> = presenter.navBus

    override fun unsubscribe() {
        presenter.unsubscribe()
    }

    fun setStartModel(model: PinViewStartModel) {
        this.id = model.pinId
    }

    override fun onSaveInstanceState(): Parcelable {
        val onSaveInstanceState = super.onSaveInstanceState()
        val bundle = Bundle()
        bundle.putParcelable("state", onSaveInstanceState)
        bundle.putString("id", id)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val bundle = state as Bundle
        id = bundle.getString("id")
        super.onRestoreInstanceState(bundle.getParcelable("state"))
    }

    override fun renderView(viewModel: PinViewModel) {
        with(viewModel.pinDto) {
            titleView.text = title
            descritionView.text = description
            locationView.text = "$lat     $lon"
            (recyclerView.adapter as PicturesAdapter).setItems(pictures)
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionManager?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        permissionManager?.onActivityResult(requestCode)
        photoHelper.onActivityResult(requestCode, resultCode, data)
    }
}