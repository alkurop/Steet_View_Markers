package com.github.alkurop.streetviewmarker

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationManager
import android.os.Handler
import android.os.Looper
import android.support.v4.graphics.BitmapCompat
import android.text.TextUtils
import android.util.Log
import android.view.SurfaceHolder
import com.google.android.gms.maps.model.LatLng
import com.squareup.picasso.Picasso
import java.util.*
import java.util.concurrent.*
import com.squareup.picasso.Target as PicassoTarget

/**
 * Created by alkurop on 31.05.16.
 */

interface IDrawThread {
    fun updateCamera(location: LatLng, bearing: Float, tilt: Float, zoom: Float)
    fun setRunning(run: Boolean)
    fun setSharingListener(listener: (Bitmap) -> Unit)
}

class DrawThread(private val surfaceHolder: SurfaceHolder,
                 val resources: Resources,
                 val markers: List<Place>,
                 val drawData: MutableList<MarkerDrawData?>,
                 val context: Context,
                 var mapsConfig: MapsConfig)
    : Thread(), IDrawThread {
    val TAG = DrawThread::class.java.simpleName
    private val matrixSet = hashSetOf<MarkerMatrixData>()
    private val bitmapMap = ConcurrentHashMap<String, Bitmap>()
    private val targetMap = ConcurrentHashMap<String, com.squareup.picasso.Target>()
    private val locBufferMap = HashMap<String, LinkedList<BufferMarkerDrawData>>()
    private val picassoHandler: Handler = Handler(Looper.getMainLooper())

    private var initX: Double = 0.0
    private var initY: Double = 0.0
    private var runFlag = false
    private var calcFlag = false
    private var mLocation: LatLng? = null
    private var mBearing: Double = 0.0
    private var mTilt: Double = 0.0
    private var mZoom: Double = 0.0
    private var screenHeight: Double = 0.0
    private var screenWidth: Double = 0.0
    private var xTransitionDim: Double = 0.0
    private var yTransitionDim: Double = 0.0
    private var xCalcAngle: Double = 0.0
    private var yCalcAngle: Double = 0.0

    private var isSharing = false
    private var mSharingListener: ((Bitmap) -> Unit)? = null

    override fun setRunning(run: Boolean) {
        runFlag = run
    }

    override fun run() {
        var canvas: Canvas? = null
        while (runFlag) {
            if (calcFlag)
                calculate()

            try {
                screenHeight = surfaceHolder.surfaceFrame.height().toDouble()
                screenWidth = surfaceHolder.surfaceFrame.width().toDouble()
                if (screenWidth > screenHeight) {
                    screenWidth = surfaceHolder.surfaceFrame.height().toDouble()
                    screenHeight = surfaceHolder.surfaceFrame.width().toDouble()
                }

                initX = (screenWidth * 0.3)
                initY = initX
                canvas = surfaceHolder.lockCanvas(null)
                synchronized(surfaceHolder) {
                    canvas?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                    if (canvas != null)
                        drawMarkersOnCanvas(canvas!!)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    if (canvas != null)
                        surfaceHolder.unlockCanvasAndPost(canvas)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun drawMarkersOnCanvas(canvas: Canvas) {
        var sharingBitmap: Bitmap? = null
        var sharingCanvas: Canvas? = null
        if (isSharing && mSharingListener != null) {
            sharingBitmap = Bitmap.createBitmap(screenWidth.toInt(), screenHeight.toInt(), Bitmap.Config.ARGB_8888)
            sharingCanvas = Canvas(sharingBitmap)
        }
        sharingCanvas?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

        val drawDataList = matrixSet
                .sortedBy { it.data.distance }
                .reversed()
                .map { matrixData ->
                    var markerData: MarkerDrawData? = null
                    val key = matrixData.data.id
                    if (matrixData.shouldShow) {
                        with(matrixData) {
                            val bitmap: Bitmap? = getBitmapFromModel(key, (data.distance * 1000).toInt())

                            val xLeft = (xLoc - (initX / 2.toDouble() * scale))
                            val yTop = yLoc - initY / 2.toDouble() * scale
                            val xRight = (xLoc + (initX / 2 * scale))
                            val yBot = yLoc + initY / 2.toDouble() * scale

                            val bufferData = BufferMarkerDrawData(xLeft, yTop, xRight, yBot)

                            val latestBufferList = if (locBufferMap.contains(key)) locBufferMap[key]!! else {
                                val mList = LinkedList<BufferMarkerDrawData>()
                                for (x in 1..5) {
                                    mList.add(bufferData)
                                }
                                locBufferMap.put(key, mList)
                                mList
                            }
                            val bufferDataCentered = BufferMarkerDrawData(xLeft, yTop, xRight, yBot)
                            latestBufferList.forEach {
                                bufferDataCentered.left += it.left
                                bufferDataCentered.top += it.top
                                bufferDataCentered.right += it.right
                                bufferDataCentered.bottom += it.bottom
                            }
                            bufferDataCentered.left /= latestBufferList.size + 1
                            bufferDataCentered.top /= latestBufferList.size + 1
                            bufferDataCentered.right /= latestBufferList.size + 1
                            bufferDataCentered.bottom /= latestBufferList.size + 1

                            latestBufferList.removeLast()
                            latestBufferList.addFirst(bufferData)

                            val rec = RectF(
                                    bufferDataCentered.left.toFloat(),
                                    bufferDataCentered.top.toFloat(),
                                    bufferDataCentered.right.toFloat(),
                                    bufferDataCentered.bottom.toFloat())

                            val paint = Paint()
                            paint.isAntiAlias = true
                            paint.isFilterBitmap = true

                            bitmap?.let {
                                if (sharingCanvas != null) {
                                    val sharePiece = Bitmap.createBitmap(bitmap)
                                    sharingCanvas?.drawBitmap(sharePiece, null, rec, paint)

                                }
                                canvas.drawBitmap(bitmap, null, rec, paint)

                                markerData = MarkerDrawData(
                                        matrixData,
                                        bufferDataCentered.left.toFloat(),
                                        bufferDataCentered.top.toFloat(),
                                        bufferDataCentered.right.toFloat(),
                                        bufferDataCentered.bottom.toFloat())
                            }
                        }
                    }
                    if (!matrixData.shouldShow) {
                        locBufferMap.remove(key)
                    }
                    markerData
                }
                .filter { it != null }
        drawData.clear()
        drawData.addAll(drawDataList)
        if (isSharing && mSharingListener != null && sharingBitmap != null) {
            isSharing = false
            Thread().run {
                mSharingListener?.invoke(sharingBitmap!!)
            }
        }
    }

    private fun MarkerMatrixData.getBitmapFromModel(key: String, distanceMeters: Int): Bitmap? {
        val containsKey = bitmapMap.containsKey(key)
        val bitmap: Bitmap? = when {
            containsKey -> bitmapMap[key]!!
            data.place.drawableRes != 0 -> {
                val bitmap = BitmapFactory.decodeResource(resources, data.place.drawableRes)
                bitmap
            }
            else -> {
                val modelBitmap = data.place.getBitmap(context, distanceMeters)
                modelBitmap?.let {
                    bitmapMap.put(key, it)
                }
                modelBitmap
            }
        }
        if (containsKey.not()) {
            data.place.markerPath?.let {
                loadPicture(key, it)
            }
        }
        return bitmap
    }

    private fun loadPicture(key: String, path: String) {
        if (!TextUtils.isEmpty(path) && !targetMap.containsKey(key)) {
            Log.d(TAG, "bitmap load started")
            val target = object : com.squareup.picasso.Target {
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                }

                override fun onBitmapFailed(errorDrawable: Drawable?) {
                    targetMap.remove(key)
                }

                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    if (bitmap != null) {
                        bitmapMap.put(key, bitmap)
                    }
                    targetMap.remove(key)
                }
            }
            targetMap.put(key, target)
            picassoHandler.post { Picasso.with(context).load(path).config(Bitmap.Config.ARGB_4444).into(target) }
        }
    }

    override fun updateCamera(location: LatLng, bearing: Float, tilt: Float, zoom: Float) {
        if (mLocation?.equals(location) != true){
            locBufferMap.clear()
            bitmapMap.clear()
        }
        mLocation = location
        mBearing = bearing.toDouble()
        mTilt = tilt.toDouble()
        mZoom = 1.0 + zoom.toDouble()
        xCalcAngle = mapsConfig.xMapCameraAngle / mZoom
        yCalcAngle = mapsConfig.yMapCameraAngle / mZoom
        calcFlag = true
        xTransitionDim = screenWidth / xCalcAngle
        yTransitionDim = screenHeight / yCalcAngle
    }

    private fun calculate() {
        if (mLocation !== null) {
            val geoData = markers.map { calculateGeoData(mLocation!!, it) }
            val data = geoData.map { generateMatrix(it) }
            matrixSet.removeAll(data)
            matrixSet.addAll(data)
        }
        calcFlag = false

    }

    private fun generateMatrix(geoData: MarkerGeoData): MarkerMatrixData {

        val isInRange = geoData.distance * 1000 <= mapsConfig.markersToShowStreetRadius
        var xLoc = 0.toDouble()
        var yLoc = 0.toDouble()
        var scale = 0.toDouble()

        var shouldShow: Boolean
        if (mBearing - geoData.azimuth > 180 || mBearing - geoData.azimuth < -180) {
            shouldShow = Math.abs(geoData.azimuth - mBearing + 360) < xCalcAngle ||
                    Math.abs(geoData.azimuth - mBearing - 360) < xCalcAngle

        } else {
            shouldShow = Math.abs(geoData.azimuth - mBearing) < xCalcAngle
        }
        if (mapsConfig.showIgnoringAzimuth) shouldShow = true
        if (!isInRange) shouldShow = false

        if (shouldShow) {
            scale = ((mapsConfig.markerScaleRadius - geoData.distance * 1000.toDouble())
                    / mapsConfig.markerScaleRadius)

            if (scale > 1) {
                scale = 1.toDouble()
            }
            if (scale <= mapsConfig.minMarkerSize &&
                    mapsConfig.markersToShowStreetRadius - geoData.distance * 1000.toDouble() > 0) {
                scale = mapsConfig.minMarkerSize

            } else if (scale <= mapsConfig.minMarkerSize) {
                scale = 0.toDouble()
            }
            scale *= (mZoom - 1) / 2 + 1
            val correctedProjectionBearing =
                    if (geoData.azimuth - mBearing >= mapsConfig.xMapCameraAngle * 1.5) {
                        mBearing + 360
                    } else if (mBearing - geoData.azimuth >= mapsConfig.xMapCameraAngle * 1.5) {
                        mBearing - 360

                    } else mBearing

            xLoc = ((geoData.azimuth - correctedProjectionBearing) * xTransitionDim) + screenWidth / 2.toDouble()

            val lowerForCloser = (mapsConfig.markersToShowStreetRadius / 2 - geoData.distance * 1000) / mapsConfig.markersToShowStreetRadius

            yLoc = (screenHeight / 2.toDouble() + (mTilt * yTransitionDim)) * (1.toDouble() - mapsConfig.yOffset + lowerForCloser / 2)

        }

        val mat = MarkerMatrixData(
                geoData,
                shouldShow,
                scale,
                xLoc,
                yLoc)
        return mat
    }

    private fun calculateGeoData(location: LatLng, place: Place): MarkerGeoData {
        val distance = calculatePlaceDistance(location, LatLng(place.location.latitude, place.location.longitude))
        val azimuth = calculatePlaceAzimuth(location, LatLng(place.location.latitude, place.location.longitude))
        return MarkerGeoData(place, distance, azimuth)
    }

    fun calculatePlaceAzimuth(myLocation: LatLng, markerLocation: LatLng): Double {
        val temp = Location(LocationManager.GPS_PROVIDER)
        temp.latitude = myLocation.latitude
        temp.longitude = myLocation.longitude
        val temp2 = Location(LocationManager.GPS_PROVIDER)
        temp2.latitude = markerLocation.latitude
        temp2.longitude = markerLocation.longitude

        val bearing = temp.bearingTo(temp2)
        return bearing.toDouble()
    }

    override fun setSharingListener(listener: (Bitmap) -> Unit) {
        mSharingListener = listener
        isSharing = true
    }
}
