package com.alkurop.mystreetplaces.ui.pin.picture.container

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.ui.base.BaseMvpActivity
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_picture.*
import javax.inject.Inject

/**
 * Created by oleksii.kuropiatnyk on 08/08/2017.
 */
class PictureActivity : BaseMvpActivity<PicturePreviewContainerStateModel>() {
    companion object {
        val START_MODEL_KEY = "start_model_key"
    }

    var alertDialog: AlertDialog? = null
    @Inject lateinit var presenter: PicturePreviewContainerPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component().inject(this)
        setupRootView(R.layout.activity_picture)

        val startModel = intent.getParcelableExtra<Bundle>(BaseMvpActivity.ARGS_KEY)
                .getParcelable<PicturePreviewContainerStateModel>(START_MODEL_KEY)

        val savedModel = savedInstanceState
                ?.getParcelable<PicturePreviewContainerStateModel>(START_MODEL_KEY)

        presenter.loadContent(savedModel ?: startModel)
        pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                presenter.onPagerPageChanged(position)
            }
        })
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = MenuInflater(this)
        inflater.inflate(R.menu.map_fragment_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        val bundle = outState ?: Bundle()
        bundle.putParcelable(START_MODEL_KEY, presenter.stateModel)
        super.onSaveInstanceState(bundle)
    }

    override fun getSubject(): Observable<PicturePreviewContainerStateModel> = presenter.viewSubject

    override fun getNavigation(): Observable<NavigationAction> = presenter.navSubject

    override fun unsubscribe() {
        presenter.unsubscribe()
        alertDialog
                ?.takeIf { it.isShowing }
                ?.dismiss()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
        }

        if (item?.itemId == R.id.deletePicture) {
            val stateModel = presenter.stateModel
            if (stateModel != null) {
                alertDialog = AlertDialog.Builder(this)
                        .setTitle(getString(R.string.delete_picture_dialog_title))
                        .setMessage(getString(R.string.delete_picture_dialog_msg))
                        .setPositiveButton(getString(R.string.yes), { _, _ ->
                            val pictureIndex = stateModel.startIndex



                        })
                        .setNegativeButton(getString(R.string.no), { _, _ -> })
                        .create()
                alertDialog?.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun renderView(viewModel: PicturePreviewContainerStateModel) {
        val adapter = PicturesPreviewAdapter(supportFragmentManager, viewModel.picturesList)
        pager.adapter = adapter
        pager.currentItem = viewModel.startIndex
    }

}