package com.alkurop.mystreetplaces.ui.search

import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatDelegate
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.data.category.mapCategory
import com.alkurop.mystreetplaces.data.search.GooglePlace
import com.alkurop.mystreetplaces.data.search.GooglePlacesSearch
import com.alkurop.mystreetplaces.domain.pin.PinDto
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.AutocompletePrediction
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.item_search.view.dataContainer
import kotlinx.android.synthetic.main.item_search.view.description
import kotlinx.android.synthetic.main.item_search.view.googleSearchList
import kotlinx.android.synthetic.main.item_search.view.icon
import kotlinx.android.synthetic.main.item_search.view.title
import timber.log.Timber

class SearchAdapter(
        val googlePlacesSearch: GooglePlacesSearch,
        val googleApiClient: GoogleApiClient
) : RecyclerView.Adapter<SearchViewHolder>() {
    var pinClickListener: ((PinDto) -> Unit)? = null
    var googlePlaceClickListener: ((GooglePlace) -> Unit)? = null

    private var items = listOf<Any>()
    lateinit var li: LayoutInflater

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        li = LayoutInflater.from(recyclerView.context)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = li.inflate(R.layout.item_search, parent, false)
        return SearchViewHolder(view, googlePlacesSearch, googleApiClient)
    }

    override fun getItemCount() = items.size

    fun updateItems(items: List<Any>) {
        val diffResult = DiffUtil.calculateDiff(SearchDiffUtil(this.items, items))
        diffResult.dispatchUpdatesTo(this)
        this.items = items
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val any = items[position]
        when (any) {
            is PinDto -> {
                holder?.bind(any, pinClickListener)
            }
            is AutocompletePrediction -> {
                holder?.bind(any, googlePlaceClickListener)
            }
        }
    }

    override fun onViewDetachedFromWindow(holder: SearchViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.unBind()
    }
}

class SearchViewHolder(
        itemView: View,
        val googlePlacesSearch: GooglePlacesSearch,
        val googleApiClient: GoogleApiClient
) : RecyclerView.ViewHolder(itemView) {

    val compositeDisposable = CompositeDisposable()

    init {
        with(itemView) {
            googleSearchList.layoutManager = LinearLayoutManager(itemView.context)
            googleSearchList.visibility = View.GONE
        }
    }

    fun bind(
            pinDto: PinDto,
            pinClickListener: ((PinDto) -> Unit)?
    ) {
        with(itemView) {
            title.text = pinDto.title
            description.text = if (pinDto.description.isEmpty().not()) pinDto.description else pinDto.address
            setOnClickListener { pinClickListener?.invoke(pinDto) }
            googleSearchList.visibility = View.GONE
            dataContainer.visibility = View.VISIBLE
            icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_image_gray_24dp))
            pinDto.categoryId.mapCategory()?.let {
                icon.setImageDrawable(ContextCompat.getDrawable(context, it.icon))
            }
            icon.visibility = View.VISIBLE

        }
    }

    fun bind(
            googlePrediction: AutocompletePrediction,
            googlePlaceClickListener: ((GooglePlace) -> Unit)?
    ) {
        with(itemView) {
            icon.visibility = View.GONE
            title.text = googlePrediction.getPrimaryText(null)
            description.text = googlePrediction.getSecondaryText(null)
            googleSearchList.visibility = View.GONE
            dataContainer.visibility = View.VISIBLE
            val subscribe = googlePlacesSearch.getPlaceListById(googlePrediction)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                               dataContainer.visibility = View.GONE
                               googleSearchList.visibility = View.VISIBLE
                               val adapter = GooglePlacesAdapter(
                                   googlePlacesSearch,
                                   googleApiClient,
                                   it,
                                   googlePlaceClickListener
                               )
                               googleSearchList.adapter = adapter
                           }, { Timber.e(it) })
            compositeDisposable.add(subscribe)
        }
    }

    fun unBind() {
    }
}
