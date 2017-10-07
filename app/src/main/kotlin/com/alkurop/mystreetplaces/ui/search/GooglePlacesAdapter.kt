package com.alkurop.mystreetplaces.ui.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.data.search.GooglePlace
import kotlinx.android.synthetic.main.item_search_google.view.*

class GooglePlacesAdapter(val items: List<GooglePlace>,
                          val googlePlaceClickListener: ((GooglePlace) -> Unit)?) : RecyclerView.Adapter<GooglePlacesAdapter.PlaceVH>() {
    lateinit var layoutInflater: LayoutInflater

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        layoutInflater = LayoutInflater.from(recyclerView.context)
    }

    override fun onBindViewHolder(holder: PlaceVH, position: Int) {
        holder.bind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PlaceVH {
        val view = layoutInflater.inflate(R.layout.item_search_google, parent, false)
        return PlaceVH(view, googlePlaceClickListener)
    }

    override fun getItemCount() = items.size

    class PlaceVH(itemView: View,
                  val googlePlaceClickListener: ((GooglePlace) -> Unit)?) : RecyclerView.ViewHolder(itemView) {
        fun bind(place: GooglePlace) {
            with(itemView) {
                icon.visibility = View.GONE
                title.text = place.name
                description.text = place.address
                setOnClickListener { googlePlaceClickListener?.invoke(place) }
            }
        }
    }
}