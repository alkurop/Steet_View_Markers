package com.alkurop.mystreetplaces.ui.pin.pictures

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

class PicturesAdapter : RecyclerView.Adapter<PicturesAdapter.PictureVH>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PictureVH {
        return PictureVH(null)
    }

    override fun getItemCount(): Int {
        return 0
    }

    override fun onBindViewHolder(holder: PictureVH?, position: Int) {
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    class PictureVH(itemView: View?) : RecyclerView.ViewHolder(itemView) {}
}