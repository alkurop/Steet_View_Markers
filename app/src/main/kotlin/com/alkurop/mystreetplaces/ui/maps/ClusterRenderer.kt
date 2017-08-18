package com.alkurop.mystreetplaces.ui.maps

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.util.DisplayMetrics
import com.alkurop.mystreetplaces.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class ClusterRenderer(val context: Context, map: GoogleMap, clusterManager: ClusterManager<MapClusterItem>) :
        DefaultClusterRenderer<MapClusterItem>(context, map, clusterManager) {
    init { minClusterSize = 20 }

    var iconBitmap: Bitmap? = null

    fun getIcon(): BitmapDescriptor? {
        if (iconBitmap == null) {

            val drawable: Drawable = ContextCompat.getDrawable(context, R.drawable.ic_pin_drop)
            val b = drawable.current as BitmapDrawable
            b.setAntiAlias(true)
            val wrap = DrawableCompat.wrap(drawable)
            iconBitmap = convertToBitmap(wrap, context.dpToPx(24), context.dpToPx(24))
        }
        return BitmapDescriptorFactory.fromBitmap(iconBitmap)
    }

    fun convertToBitmap(drawable: Drawable, widthPixels: Int, heightPixels: Int): Bitmap {
        val mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(mutableBitmap)
        drawable.setBounds(0, 0, widthPixels, heightPixels)
        drawable.draw(canvas)
        return mutableBitmap
    }

    fun Context.dpToPx(dp: Int): Int {
        val displayMetrics = this.resources.displayMetrics
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }
}
