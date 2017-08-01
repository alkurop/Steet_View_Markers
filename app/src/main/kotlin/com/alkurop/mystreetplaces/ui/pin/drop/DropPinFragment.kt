package com.alkurop.mystreetplaces.ui.pin.drop

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.ui.base.BaseMvpFragment
import com.alkurop.mystreetplaces.ui.navigation.NavigationAction
import com.google.android.gms.maps.model.LatLng
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_drop_pin.*
import javax.inject.Inject

class DropPinFragment : BaseMvpFragment<DropPinViewModel>() {
    val compositeDesposable = CompositeDisposable()

    companion object {
        val LOCATION_KEY = "location"

        fun getNewInstance(location: LatLng): Fragment {
            val fragment = DropPinFragment()
            val args = Bundle()
            args.putParcelable(LOCATION_KEY, location)
            fragment.arguments = args
            return fragment
        }
    }

    @Inject lateinit var presenter: DropPinPresenter

    override fun getSubject(): Observable<DropPinViewModel> = presenter.viewBus

    override fun getNavigation(): Observable<NavigationAction> = presenter.navBus

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        component().inject(this)
        return inflater?.inflate(R.layout.fragment_drop_pin, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val location = arguments.getParcelable<LatLng>(LOCATION_KEY)
        presenter.start(location)
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
        compositeDesposable.addAll(disposable, disposable1)
        submit.setOnClickListener { presenter.submit() }
    }

    override fun unsubscribe() {
        presenter.unsubscribe()
        compositeDesposable.clear()
    }

    override fun renderView(viewModel: DropPinViewModel) {
        with(viewModel) {
            pinDto?.let { pin ->
                location.text = "lat = ${pin.lat} lon = ${pin.lon}"
                title.setText(pin.title)
                description.setText(pin.description)
            }

        }
    }
}