package com.alkurop.mystreetplaces.ui.pin.picture

import android.os.Bundle
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.ui.base.BaseMvpActivity
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by oleksii.kuropiatnyk on 08/08/2017.
 */
class PictureActivity : BaseMvpActivity<PicturePreviewActivityModel>() {
    companion object {
        val START_MODEL_KEY = "start_model_key"
    }

    @Inject lateinit var presenter: PicturePreviewPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component().inject(this)
        setupRootView(R.layout.activity_picture)

        val startModel  = intent.getParcelableExtra<Bundle>(BaseMvpActivity.ARGS_KEY)
                .getParcelable<PicturePreviewStateModel>(START_MODEL_KEY)
        presenter.loadContent(startModel)
    }

    override fun getSubject(): Observable<PicturePreviewActivityModel> = presenter.viewSubject

    override fun getNavigation(): Observable<NavigationAction> = presenter.navSubject

    override fun unsubscribe() {
        presenter.unsubscribe()
    }

    override fun renderView(viewModel: PicturePreviewActivityModel) {

    }
}