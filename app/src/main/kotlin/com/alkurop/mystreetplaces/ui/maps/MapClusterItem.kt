package com.alkurop.mystreetplaces.ui.maps

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v4.graphics.drawable.DrawableCompat
import android.util.DisplayMetrics
import com.alkurop.mystreetplaces.R
import com.github.alkurop.streetviewmarker.Place
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class MapClusterItem(val place: Place) : ClusterItem {
    override fun getSnippet(): String {
        return "snippet marker_id = ${place.id}"
    }

    override fun getTitle(): String {
        return "marker_id = ${place.id}"
    }

    override fun getPosition(): LatLng? {
        try {
            return LatLng(place.location.latitude, place.location.longitude)
        } catch(e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    class CustomClusterRenderer(val context: Context, map: GoogleMap, clusterManager: ClusterManager<MapClusterItem>) :
            DefaultClusterRenderer<MapClusterItem>(context, map, clusterManager) {
        var iconBitmap: Bitmap? = null

        override fun getMarker(clusterItem: MapClusterItem): Marker? {
            return super.getMarker(clusterItem)
        }


        fun getIcon(): BitmapDescriptor? {
            if (iconBitmap == null) {

                val drawable: Drawable = context.resources.getDrawable(R.drawable.ic_pin_drop)
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
}