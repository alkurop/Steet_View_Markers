package com.alkurop.mystreetplaces.ui.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.data.search.GooglePlace
import com.alkurop.mystreetplaces.data.search.GooglePlacesSearch
import com.google.android.gms.common.api.GoogleApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.item_search_google.view.*
import timber.log.Timber

class GooglePlacesAdapter(
        val googlePlacesSearch: GooglePlacesSearch,
        val googleApiClient: GoogleApiClient,
        val items: List<GooglePlace>,
        val googlePlaceClickListener: ((GooglePlace) -> Unit)?
) : RecyclerView.Adapter<GooglePlacesAdapter.PlaceVH>() {
    lateinit var layoutInflater: LayoutInflater

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        layoutInflater = LayoutInflater.from(recyclerView.context)
    }

    override fun onBindViewHolder(holder: PlaceVH, position: Int) {
        holder.bind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceVH {
        val view = layoutInflater.inflate(R.layout.item_search_google, parent, false)
        return PlaceVH(view, googlePlacesSearch, googleApiClient, googlePlaceClickListener)
    }

    override fun getItemCount() = items.size

    override fun onViewRecycled(holder: PlaceVH) {
        super.onViewRecycled(holder)
        holder.unbind()
    }

    class PlaceVH(
            itemView: View,
            val googlePlacesSearch: GooglePlacesSearch,
            val googleApiClient: GoogleApiClient,
            val googlePlaceClickListener: ((GooglePlace) -> Unit)?
    ) : RecyclerView.ViewHolder(itemView) {

        val compositeDisposable = CompositeDisposable()

        fun bind(place: GooglePlace) {
            with(itemView) {
                icon.visibility = View.VISIBLE
                title.text = place.name
                description.text = place.address

                setOnClickListener { googlePlaceClickListener?.invoke(place) }
                val disposable = googlePlacesSearch.getPlacePicturesMetadata(place)
                    .toObservable().filter { !it.isEmpty() }
                    .map { it.first() }
                    .flatMap {
                        googlePlacesSearch.getPhotoScaled(googleApiClient, it, 100, 100)
                            .toObservable()
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                                   icon.setImageBitmap(it)

                               }, { Timber.e(it) })
                compositeDisposable.add(disposable)
            }

        }

        fun unbind() {
            compositeDisposable.clear()
        }
    }
}
