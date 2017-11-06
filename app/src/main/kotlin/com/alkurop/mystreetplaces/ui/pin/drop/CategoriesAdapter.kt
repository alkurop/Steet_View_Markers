package com.alkurop.mystreetplaces.ui.pin.drop

import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.data.category.PinCategory
import kotlinx.android.synthetic.main.item_category.view.*

class CategoriesAdapter(val onCategorySelectedListener: (PinCategory) -> Unit) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    val categoryArray = PinCategory.values()
    lateinit var li: LayoutInflater

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        li = LayoutInflater.from(recyclerView.context)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CategoryViewHolder {
        val view = li.inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view, onCategorySelectedListener)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder?, position: Int) {
        holder?.bind(categoryArray[position])
    }

    override fun getItemCount() = categoryArray.size

    inner class CategoryViewHolder(itemView: View?,
                                   val onCategorySelectedListener: (PinCategory) -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun bind(pinCategory: PinCategory) {
            itemView.imageView.setImageDrawable(
                    ContextCompat.getDrawable(itemView.context, pinCategory.icon))
            itemView.imageView.setOnClickListener({onCategorySelectedListener.invoke(pinCategory)})
        }
    }
}