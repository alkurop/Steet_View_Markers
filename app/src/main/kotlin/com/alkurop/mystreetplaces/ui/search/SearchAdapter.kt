package com.alkurop.mystreetplaces.ui.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.domain.pin.PinDto
import kotlinx.android.synthetic.main.item_search.view.*
import android.support.v7.util.DiffUtil

class SearchAdapter(val onClickListener:(PinDto) -> Unit) : RecyclerView.Adapter<SearchViewHolder>() {
    private var items = listOf<PinDto>()
    lateinit var li: LayoutInflater

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        li = LayoutInflater.from(recyclerView.context)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SearchViewHolder {
        val view = li.inflate(R.layout.item_search, parent, false)
        return SearchViewHolder(view, onClickListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(items: List<PinDto>) {
        val diffResult = DiffUtil.calculateDiff(SearchDiffUtil(this.items, items))
        diffResult.dispatchUpdatesTo(this)
        this.items = items
    }

    override fun onBindViewHolder(holder: SearchViewHolder?, position: Int) {
        holder?.bind(items[position])
    }
}

class SearchViewHolder(itemView: View, val onClickListener:(PinDto) -> Unit) : RecyclerView.ViewHolder(itemView) {
    fun bind(pinDto: PinDto) {
        itemView.title.text = pinDto.title
        itemView.description.text = pinDto.description
        itemView.setOnClickListener { onClickListener.invoke(pinDto) }
    }
}