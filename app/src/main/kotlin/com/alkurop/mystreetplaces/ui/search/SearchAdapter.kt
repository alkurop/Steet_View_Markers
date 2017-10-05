package com.alkurop.mystreetplaces.ui.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.domain.pin.PinDto
import kotlinx.android.synthetic.main.item_search.view.*
import android.support.v7.util.DiffUtil
import com.google.android.gms.location.places.AutocompletePrediction

class SearchAdapter : RecyclerView.Adapter<SearchViewHolder>() {
    var pinClickListener: ((PinDto) -> Unit)? = null
    var predictionClickListener: ((AutocompletePrediction) -> Unit)? = null
    private var items = listOf<Any>()
    lateinit var li: LayoutInflater

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        li = LayoutInflater.from(recyclerView.context)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SearchViewHolder {
        val view = li.inflate(R.layout.item_search, parent, false)
        return SearchViewHolder(view)
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
                holder?.bind(any, predictionClickListener)
            }
        }
    }
}

class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(pinDto: PinDto,
             pinClickListener: ((PinDto) -> Unit)?) {
        itemView.title.text = pinDto.title
        itemView.description.text = pinDto.description
        itemView.setOnClickListener { pinClickListener?.invoke(pinDto) }
    }

    fun bind(googlePrediction: AutocompletePrediction,
             predictionClickListener: ((AutocompletePrediction) -> Unit)?) {
        itemView.title.text = googlePrediction.getPrimaryText(null)
        itemView.description.text = googlePrediction.getSecondaryText(null)
        itemView.setOnClickListener { predictionClickListener?.invoke(googlePrediction) }

    }
}