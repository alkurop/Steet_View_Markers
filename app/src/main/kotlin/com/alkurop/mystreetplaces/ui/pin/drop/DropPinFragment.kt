package com.alkurop.mystreetplaces.ui.pin.drop

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.ui.base.BaseMvpFragment
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.alkurop.mystreetplaces.utils.CameraPictureHelper
import com.alkurop.mystreetplaces.utils.CameraPictureHelperImpl
import com.github.alkurop.jpermissionmanager.PermissionOptionalDetails
import com.github.alkurop.jpermissionmanager.PermissionsManager
import com.google.android.gms.maps.model.LatLng
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_drop_pin.*
import javax.inject.Inject

class DropPinFragment : BaseMvpFragment<DropPinViewModel>() {
    companion object {
        val LOCATION_KEY = "location_key"
        val ID_KEY = "id_key"

        fun getNewInstance(location: LatLng): Fragment {
            val fragment = DropPinFragment()
            val args = Bundle()
            args.putParcelable(LOCATION_KEY, location)
            fragment.arguments = args
            return fragment
        }

        fun getNewInstance(id: String): Fragment {
            val fragment = DropPinFragment()
            val args = Bundle()
            args.putString(ID_KEY, id)
            fragment.arguments = args
            return fragment
        }
    }

    val viewDisposable = CompositeDisposable()
    lateinit var photoHelper: CameraPictureHelper
    @Inject lateinit var presenter: DropPinPresenter
    var permissionManager: PermissionsManager? = null
    var alert: AlertDialog? = null

    override fun getSubject(): Observable<DropPinViewModel> = presenter.viewBus

    override fun getNavigation(): Observable<NavigationAction> = presenter.navBus

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        component().inject(this)
        return inflater?.inflate(R.layout.fragment_drop_pin, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val location = arguments.getParcelable<LatLng>(LOCATION_KEY)
        location?.let { presenter.start(location) }

        val pinId = arguments.getString(ID_KEY)
        pinId?.let { presenter.start(pinId) }
        photoHelper = CameraPictureHelperImpl(this)

        submit.setOnClickListener { presenter.submit() }
        delete.setOnClickListener {
            AlertDialog.Builder(activity)
                    .setTitle("Delete pin")
                    .setMessage("Would you like to delete this pin?")
                    .setPositiveButton(android.R.string.ok, { _, _ ->
                        presenter.deletePin()
                    })
                    .setNegativeButton(android.R.string.cancel, { _, _ -> })
                    .show()
        }
        delete.visibility = View.GONE

        addPicture.setOnClickListener {
            setUpPermissionsManager({
                photoHelper.execute({ file ->
                    presenter.onAddPicture(file)
                })
            })
        }

    }

    private fun setUpPermissionsManager(function: () -> Unit) {
        if (permissionManager == null) permissionManager = PermissionsManager(this)
        val permission1 = Pair(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                PermissionOptionalDetails(getString(R.string.storage_permission_rationale_title),
                        getString(R.string.storage_permission_rationale)))
        val permission2 = Pair(Manifest.permission.CAMERA,
                PermissionOptionalDetails(getString(R.string.camera_permission_rationale_title),
                        getString(R.string.camera_permission_rationale)))
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

    override fun unsubscribe() {
        presenter.unsubscribe()
        viewDisposable.clear()
        alert?.dismiss()
    }

    override fun renderView(viewModel: DropPinViewModel) {
        viewDisposable.clear()
        with(viewModel) {
            pinDto?.let { pin ->
                location.text = "lat = ${pin.lat} lon = ${pin.lon}"
                title.setText(pin.title)
                description.setText(pin.description)
                pin.id?.let { delete.visibility = View.VISIBLE }
            }
        }
        val disposable = RxTextView.textChangeEvents(title)
                .subscribe({
                    val text = it.text().toString()
                    presenter.onTitleChange(text)
                })
        val disposable1 = RxTextView.textChangeEvents(description)
                .subscribe({
                    val text = it.text().toString()
                    presenter.onDescriptionChange(text)
                })
        viewDisposable.addAll(disposable, disposable1)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        permissionManager?.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        permissionManager?.onActivityResult(requestCode)
        photoHelper.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}