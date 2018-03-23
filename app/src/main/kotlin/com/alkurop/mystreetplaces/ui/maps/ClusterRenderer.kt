package com.alkurop.mystreetplaces.ui.maps

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class ClusterRenderer(val context: Context, map: GoogleMap, clusterManager: ClusterManager<MapClusterItem>) :
        DefaultClusterRenderer<MapClusterItem>(context, map, clusterManager) {
    init {
        minClusterSize = 20
    }

    fun convertToBitmap(drawable: Drawable, widthPixels: Int, heightPixels: Int): Bitmap {
        val mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(mutableBitmap)
        val paint = Paint()
        paint.color = Color.GREEN
        canvas.drawCircle(widthPixels.toFloat() / 2, widthPixels.toFloat() / 2, widthPixels.toFloat() / 2, paint)
        drawable.setBounds(1, 1, widthPixels - 1, heightPixels - 1)
        drawable.draw(canvas)
        return mutableBitmap
    }

    fun Context.dpToPx(dp: Int): Int {
        val displayMetrics = this.resources.displayMetrics
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    override fun onBeforeClusterItemRendered(item: MapClusterItem, markerOptions: MarkerOptions) {
        item.place.category?.let {
            val iconBitmap = convertToBitmap(ContextCompat.getDrawable(context, it.icon)!!, context.dpToPx(32), context.dpToPx(32))
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconBitmap))
        }
    }
}
