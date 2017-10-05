package com.alkurop.mystreetplaces.ui.search

import android.support.v7.util.DiffUtil
import com.alkurop.mystreetplaces.domain.pin.PinDto
import com.google.android.gms.location.places.AutocompletePrediction

class SearchDiffUtil(val oldList: List<Any>, val newList: List<Any>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return if (oldItem is PinDto && newItem is PinDto) {
            oldItem.id == newItem.id
        } else if (oldItem is AutocompletePrediction && newItem is AutocompletePrediction) {
            oldItem.placeId == newItem.placeId
        } else {
            false
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return if (oldItem is PinDto && newItem is PinDto) {
            oldItem.timeStamp == newItem.timeStamp
        } else areItemsTheSame(oldItemPosition, newItemPosition)
    }
}