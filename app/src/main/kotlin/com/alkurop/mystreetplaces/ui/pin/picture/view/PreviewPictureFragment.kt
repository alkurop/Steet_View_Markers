package com.alkurop.mystreetplaces.ui.pin.picture.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.data.pin.PictureWrapper
import com.alkurop.mystreetplaces.ui.base.BaseMvpFragment
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.alkurop.mystreetplaces.ui.navigation.NoArgsNavigation
import com.nostra13.universalimageloader.core.ImageLoader
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_picture_preview.*
import javax.inject.Inject

class PreviewPictureFragment : BaseMvpFragment<PreviewPictureModel>() {
    companion object {
        val PICTURE_WRAPPER_KEY = "picture_wrapper_key"
    }

    @Inject lateinit var presenter: PreviewPicturePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_picture_preview, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val model = arguments.getParcelable<PictureWrapper>(PICTURE_WRAPPER_KEY)
        presenter.start(model)
    }

    override fun getSubject(): Observable<PreviewPictureModel> = presenter.viewSubject

    override fun getNavigation(): Observable<NavigationAction> = presenter.navSubject

    override fun unsubscribe() {
        presenter.unsubscribe()
    }

    override fun renderView(viewModel: PreviewPictureModel) {
        val imagePath = viewModel.pictureWrapper.localPhoto ?: viewModel.pictureWrapper.serverPhoto
        ImageLoader.getInstance().displayImage(imagePath, image_view)
    }

    override fun navigate(it: NavigationAction) {
        super.navigate(it)
        if (it.equals(NoArgsNavigation.REMOVE_ACTION)) {
        }
    }

}