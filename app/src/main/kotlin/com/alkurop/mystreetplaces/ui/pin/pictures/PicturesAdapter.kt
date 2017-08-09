package com.alkurop.mystreetplaces.ui.pin.pictures

import android.graphics.Bitmap
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.data.pin.PictureWrapper
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener
import kotlinx.android.synthetic.main.item_add_picture.view.*
import kotlinx.android.synthetic.main.item_existing_picture.view.*

class PicturesAdapter : RecyclerView.Adapter<PicturesAdapter.PictureVH>() {
    private var pictures = listOf<PictureWrapper>()

    lateinit var li: LayoutInflater

    var onAddPictureClick: (() -> Unit)? = null
    var onPictureClick: ((Int) -> Unit)? = null

    fun setItems(newPictures: List<PictureWrapper>) {
        val diffCallback = PicturesDiffUtil(pictures, newPictures)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        pictures = newPictures
    }

    fun getItems() = pictures

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
        li = LayoutInflater.from(recyclerView?.context)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PictureVH {
        return if (viewType == 0 && getOffset() > 0) createAddPictureVh(parent)
        else createExistingPictureVh(parent)
    }

    private fun createExistingPictureVh(parent: ViewGroup?): ExistingPictureVh {
        val view = li.inflate(R.layout.item_existing_picture, parent, false)
        return ExistingPictureVh(view)
    }

    private fun createAddPictureVh(parent: ViewGroup?): AddPictureVh {
        val view = li.inflate(R.layout.item_add_picture, parent, false)
        return AddPictureVh(view)
    }

    override fun getItemCount(): Int {
        return pictures.size + getOffset()
    }

    fun getItem(position: Int): PictureWrapper = pictures[position - getOffset()]

    private fun getOffset() = if (onAddPictureClick == null) 0 else 1

    override fun onBindViewHolder(holder: PictureVH?, position: Int) {
        if (holder is ExistingPictureVh) {
            holder.bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    abstract class PictureVH(view: View?) : RecyclerView.ViewHolder(view)

    inner class AddPictureVh(view: View?) : PictureVH(view) {
        init {
            itemView.addContainer.setOnClickListener {
                onAddPictureClick?.invoke()
            }
        }
    }

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
                    itemView.photoView.setImageResource(R.mipmap.ic_launcher_round)
                }
            })
        }
    }

    override fun onViewRecycled(holder: PictureVH?) {
        super.onViewRecycled(holder)
        if (holder is ExistingPictureVh) {
            ImageLoader.getInstance().cancelDisplayTask(holder.itemView.photoView)
        }
    }
}