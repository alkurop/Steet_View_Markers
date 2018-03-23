package com.alkurop.mystreetplaces.ui.pin.pictures

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.data.pin.PictureWrapper
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener
import kotlinx.android.synthetic.main.item_existing_picture.view.photoView
import kotlinx.android.synthetic.main.item_existing_picture.view.pictureContainer
import kotlinx.android.synthetic.main.item_existing_picture.view.progress_bar

class PicturesAdapter : RecyclerView.Adapter<PicturesAdapter.PictureVH>() {
    private var pictures = listOf<PictureWrapper>()

    lateinit var li: LayoutInflater

    var onPictureClick: ((Int) -> Unit)? = null

    fun setItems(newPictures: List<PictureWrapper>) {
        pictures = newPictures
        notifyDataSetChanged()
    }

    fun getItems() = pictures

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        li = LayoutInflater.from(recyclerView?.context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureVH {
        return createExistingPictureVh(parent)
    }

    private fun createExistingPictureVh(parent: ViewGroup?): ExistingPictureVh {
        val view = li.inflate(R.layout.item_existing_picture, parent, false)
        return ExistingPictureVh(view)
    }

    override fun getItemCount(): Int {
        return pictures.size
    }

    fun getItem(position: Int): PictureWrapper = pictures[position]


    override fun onBindViewHolder(holder: PictureVH, position: Int) {
        if (holder is ExistingPictureVh) {
            holder.bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    abstract class PictureVH(view: View?) : RecyclerView.ViewHolder(view)

    inner class ExistingPictureVh(view: View?) : PictureVH(view) {
        init {
            itemView.pictureContainer.setOnClickListener {
                onPictureClick?.invoke(adapterPosition)
            }
        }

        fun bind(item: PictureWrapper) {
            val imagePath = item.localPhoto ?: item.serverPhoto
            ImageLoader.getInstance().displayImage(imagePath, itemView.photoView, object : SimpleImageLoadingListener() {
                override fun onLoadingStarted(imageUri: String?, view: View?) {
                    itemView.progress_bar.visibility = View.VISIBLE
                }

                override fun onLoadingComplete(imageUri: String?, view: View?, loadedImage: Bitmap?) {
                    itemView.progress_bar.visibility = View.GONE
                }

                override fun onLoadingFailed(imageUri: String?, view: View?, failReason: FailReason?) {
                    itemView.progress_bar.visibility = View.GONE
                }
            })
        }
    }

    override fun onViewRecycled(holder: PictureVH) {
        super.onViewRecycled(holder)
        if (holder is ExistingPictureVh) {
            ImageLoader.getInstance().cancelDisplayTask(holder.itemView.photoView)
        }
    }
}
