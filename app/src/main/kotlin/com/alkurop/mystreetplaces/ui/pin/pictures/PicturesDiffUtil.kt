package com.alkurop.mystreetplaces.ui.pin.pictures

import android.support.v7.util.DiffUtil
import com.alkurop.mystreetplaces.data.pin.PictureWrapper

class PicturesDiffUtil(val oldList: List<PictureWrapper>, val newList: List<PictureWrapper>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.timeStamp == newItem.timeStamp
    }
}