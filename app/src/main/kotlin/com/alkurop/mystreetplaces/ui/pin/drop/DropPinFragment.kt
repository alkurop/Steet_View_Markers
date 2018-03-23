package com.alkurop.mystreetplaces.ui.pin.drop

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.data.category.mapCategory
import com.alkurop.mystreetplaces.data.pin.PictureWrapper
import com.alkurop.mystreetplaces.ui.base.BaseMvpFragment
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.alkurop.mystreetplaces.ui.pin.picture.container.PictureActivity
import com.alkurop.mystreetplaces.ui.pin.picture.container.PicturePreviewContainerStateModel
import com.alkurop.mystreetplaces.ui.pin.pictures.PicturesAdapter
import com.alkurop.mystreetplaces.utils.CameraPictureHelper
import com.alkurop.mystreetplaces.utils.CameraPictureHelperImpl
import com.alkurop.mystreetplaces.utils.MediaPicker
import com.alkurop.mystreetplaces.utils.MediaType
import com.github.alkurop.jpermissionmanager.PermissionOptionalDetails
import com.github.alkurop.jpermissionmanager.PermissionsManager
import com.google.android.gms.maps.model.LatLng
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_drop_pin.categories
import kotlinx.android.synthetic.main.fragment_drop_pin.categoryImageView
import kotlinx.android.synthetic.main.fragment_drop_pin.deleteOption
import kotlinx.android.synthetic.main.fragment_drop_pin.description
import kotlinx.android.synthetic.main.fragment_drop_pin.galeryPicture
import kotlinx.android.synthetic.main.fragment_drop_pin.location_view
import kotlinx.android.synthetic.main.fragment_drop_pin.picture
import kotlinx.android.synthetic.main.fragment_drop_pin.recyclerView
import kotlinx.android.synthetic.main.fragment_drop_pin.title
import java.io.File
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
    var permissionManager: PermissionsManager? = null
    @Inject
    lateinit var presenter: DropPinPresenter
    var alert: AlertDialog? = null
    var shouldReturnResult = false
    var filePicker: MediaPicker? = null
    var mediauri: Uri? = null
    var pendingType: MediaType? = null
    var selectedType: MediaType? = null
    var isSubmitVisible = false

    override fun getSubject(): Observable<DropPinViewModel> = presenter.viewBus

    override fun getNavigation(): Observable<NavigationAction> = presenter.navBus

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        component().inject(this)
        return inflater?.inflate(R.layout.fragment_drop_pin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        photoHelper = CameraPictureHelperImpl(this)
        photoHelper.setRequestCode(3021)

        setHasOptionsMenu(true)


        deleteOption.setOnClickListener {
            AlertDialog.Builder(activity!!)
                .setTitle(getString(R.string.delete_pin_title))
                .setMessage(getString(R.string.delet_pin_msg))
                .setPositiveButton(android.R.string.ok, { _, _ ->
                    presenter.deletePin()
                })
                .setNegativeButton(android.R.string.cancel, { _, _ -> })
                .show()
        }

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        val picturesAdapter = PicturesAdapter()

        picturesAdapter.onPictureClick = { presenter.onPictureClick(it, picturesAdapter.getItems()) }

        recyclerView.adapter = picturesAdapter
        val location = arguments?.getParcelable<LatLng>(LOCATION_KEY)
        location?.let { presenter.start(location) }

        val categoriesLayoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
        categories.layoutManager = categoriesLayoutManager
        categories.setHasFixedSize(true)
        categories.adapter = CategoriesAdapter {
            presenter.onCategorySelected(it)
        }

        val pinId = arguments?.getString(ID_KEY)
        pinId?.let { presenter.start(pinId) }

        filePicker = MediaPicker { it ->
            val map: HashMap<String, PermissionOptionalDetails> = HashMap()
            map.put(it, PermissionOptionalDetails(getString(R.string.storage_permission_rationale_title), getString(R.string.storage_permission_rationale)))
            if (permissionManager == null) permissionManager = PermissionsManager(this)
            permissionManager?.setRequestCode(202)
            permissionManager?.clearPermissions()
            permissionManager?.clearPermissionsListeners()
            permissionManager?.addPermissions(map)
            permissionManager?.addPermissionsListener { }
            permissionManager?.makePermissionRequest()
        }
        galeryPicture.setOnClickListener {
            filePicker?.fromGallery(this, MediaType.PHOTO)
        }
        picture.setOnClickListener {
            setUpPermissionsManager {
                photoHelper.execute({ file ->
                                        presenter.onAddPicture(file)
                                    })
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.drop_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        menu?.findItem(R.id.submitOption)?.isVisible = isSubmitVisible
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.submitOption) {
            presenter.submit()

        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpPermissionsManager(function: () -> Unit) {
        if (permissionManager == null) permissionManager = PermissionsManager(this)
        permissionManager?.setRequestCode(202)
        val permission1 = Pair(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            PermissionOptionalDetails(
                getString(R.string.storage_permission_rationale_title),
                getString(R.string.storage_permission_rationale)
            )
        )
        val permission2 = Pair(
            Manifest.permission.CAMERA,
            PermissionOptionalDetails(
                getString(R.string.camera_permission_rationale_title),
                getString(R.string.camera_permission_rationale)
            )
        )
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
                location_view.setText(pin.address ?: "")
                title.setText(pin.title)
                isSubmitVisible = pin.title.isNotEmpty()
                activity?.invalidateOptionsMenu()
                description.setText(pin.description)
                pin.id?.let {
                    deleteOption.visibility = View.VISIBLE
                }
                pin.id?.let { (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.edit_pin) }
                (recyclerView.adapter as PicturesAdapter).setItems(pin.pictures)
                val caterory = pin.categoryId.mapCategory()
                caterory?.let {
                    val drawable = ContextCompat.getDrawable(context!!, it.icon)
                    categoryImageView.setImageDrawable(drawable)
                }
            }
        }
        RxTextView.textChangeEvents(title)
            .observeOn(Schedulers.io())
            .subscribe({
                           val text = it.text().toString()
                           presenter.onTitleChange(text)
                           isSubmitVisible = text.isNotEmpty()
                           activity?.invalidateOptionsMenu()
                       })
        RxTextView.textChangeEvents(description)
            .observeOn(Schedulers.io())
            .subscribe({
                           val text = it.text().toString()
                           presenter.onDescriptionChange(text)
                       })
        RxTextView.textChangeEvents(location_view)
            .observeOn(Schedulers.io())
            .subscribe({
                           val text = it.text().toString()
                           presenter.onAddressTextChange(text)
                       })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        permissionManager?.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        permissionManager?.onActivityResult(requestCode)
        photoHelper.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PictureActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val pictureModel = data.getParcelableExtra<PicturePreviewContainerStateModel>(PictureActivity.START_MODEL_KEY)
            pictureModel?.let { reloadList(it.picturesList) }
            shouldReturnResult = true
        }
        filePicker?.handleResult(requestCode, resultCode, data, { uri ->
            mediauri = uri
            selectedType = pendingType
            applyUri()
        })

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun applyUri() {
        mediauri?.let {
            presenter.onAddPicture(File(it.path))
        }
    }

    private fun reloadList(pictures: List<PictureWrapper>) {
        presenter.reloadPictureList(pictures)
    }

    override fun onBackward() {
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }
}
