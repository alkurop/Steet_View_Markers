package com.alkurop.mystreetplaces.ui.search

import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.data.search.GooglePlace
import com.alkurop.mystreetplaces.data.search.GooglePlacesSearch
import com.alkurop.mystreetplaces.domain.pin.PinDto
import com.google.android.gms.location.places.AutocompletePrediction
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.item_search.view.*
import timber.log.Timber

class SearchAdapter(val googlePlacesSearch: GooglePlacesSearch) : RecyclerView.Adapter<SearchViewHolder>() {
    var pinClickListener: ((PinDto) -> Unit)? = null
    var googlePlaceClickListener: ((GooglePlace) -> Unit)? = null

    private var items = listOf<Any>()
    lateinit var li: LayoutInflater

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        li = LayoutInflater.from(recyclerView.context)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SearchViewHolder {
        val view = li.inflate(R.layout.item_search, parent, false)
        return SearchViewHolder(view, googlePlacesSearch)
    }

    override fun getItemCount() = items.size

    fun updateItems(items: List<Any>) {
        val diffResult = DiffUtil.calculateDiff(SearchDiffUtil(this.items, items))
        diffResult.dispatchUpdatesTo(this)
        this.items = items
    }

    override fun onBindViewHolder(holder: SearchViewHolder?, position: Int) {
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

class SearchViewHolder(itemView: View,
                       val googlePlacesSearch: GooglePlacesSearch)
    : RecyclerView.ViewHolder(itemView) {

    var isCollapsed = false
    val compositeDisposable = CompositeDisposable()
    var itemList: List<GooglePlacesSearch>? = null

    init {
        with(itemView) {
            googleSearchList.layoutManager = LinearLayoutManager(itemView.context)
            chevron.visibility = View.GONE
            googleSearchList.visibility = View.GONE
        }
    }

    fun bind(pinDto: PinDto,
             pinClickListener: ((PinDto) -> Unit)?) {
        with(itemView) {
            title.text = pinDto.title
            description.text = pinDto.description
            setOnClickListener { pinClickListener?.invoke(pinDto) }
            chevron.visibility = View.GONE
            googleSearchList.visibility = View.GONE
        }
    }

    fun bind(googlePrediction: AutocompletePrediction,
             googlePlaceClickListener: ((GooglePlace) -> Unit)?
    ) {
        with(itemView) {
            title.text = googlePrediction.getPrimaryText(null)
            description.text = googlePrediction.getSecondaryText(null)
            chevron.visibility = View.VISIBLE
            setOnClickListener {
                isCollapsed = !isCollapsed
                if (isCollapsed) {
                    googleSearchList.visibility = View.VISIBLE
                    chevron.rotation = 90f
                } else {
                    googleSearchList.visibility = View.GONE
                    chevron.rotation = -90f
                }
            }
            itemList?.let { }
            if (itemList == null) {
                val subscribe = googlePlacesSearch.getPlaceListById(googlePrediction)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            val adapter = GooglePlacesAdapter(it, googlePlaceClickListener)
                            googleSearchList.adapter = adapter
                        }, { Timber.e(it) })
                compositeDisposable.add(subscribe)

            }
        }
    }

    fun unBind() {
        compositeDisposable.clear()
    }
}