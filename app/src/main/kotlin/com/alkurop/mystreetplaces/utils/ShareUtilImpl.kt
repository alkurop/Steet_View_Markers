package com.alkurop.mystreetplaces.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Environment
import android.support.v4.content.FileProvider
import com.alkurop.mystreetplaces.R
import com.alkurop.mystreetplaces.domain.pin.PinDto
import com.github.alkurop.streetviewmarker.CameraPosition
import com.github.alkurop.streetviewmarker.StreetMarkerView
import com.google.android.gms.maps.model.LatLng
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import java.io.File
import java.io.FileOutputStream
import com.squareup.picasso.Target as PicassoTarget

class ShareUtilImpl(val context: Context) : ShareUtil {

    override fun createShareIntentFromPin(pin: PinDto): Intent {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.sharing_street_view_subject))
        intent.putExtra(Intent.EXTRA_TEXT, getPinSharingText(context, pin))
        return intent
    }

    override fun createShareIntentFromStreetProjection(markerView: StreetMarkerView, cameraPosition: CameraPosition): Observable<Intent> {
        val streetViewObservable = Observable.create<Bitmap> {
            val target = object : PicassoTarget {
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                }

                override fun onBitmapFailed(errorDrawable: Drawable?) {
                    it.onError(IllegalStateException("Picasso could not load bitmap"))
                }

                override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom?) {
                    it.onNext(bitmap)
                    it.onComplete()
                }
            }

            val pathBuilder = StringBuilder()
            pathBuilder.append("https://maps.googleapis.com/maps/api/streetview?")
                    .append("size=400x800")
                    .append("&location=${cameraPosition.location.latitude},${cameraPosition.location.longitude}")
                    .append("&fov=70")
                    .append("&heading=${cameraPosition.camera.bearing}")
                    .append("&pitch=${cameraPosition.camera.tilt}")
            markerView.post {
                Picasso.with(context).load(pathBuilder.toString()).into(target)
            }
        }

        val markerViewObservable = Observable.create<Bitmap> { subscriber ->
            try {
                markerView.setSharingListener {
                    subscriber.onNext(it)
                    subscriber.onComplete()
                }
            } catch (e: Exception) {
                subscriber.onError(e)
            }
        }

        val resultObservable = Observable.zip(streetViewObservable, markerViewObservable,
                BiFunction<Bitmap, Bitmap, Bitmap> { street, markers ->
                    val combineBitmap = combineBitmap(street, markers)
                    combineBitmap
                })

        return Observable.fromCallable {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(android.content.Intent.EXTRA_TEXT, getStreetSharingText(context, cameraPosition.location))
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, context.getString(R.string.sharing_street_view_subject))
            intent
        }
    }

    fun combineBitmap(background: Bitmap, foreground: Bitmap): Bitmap {
        val result = Bitmap.createBitmap(foreground.width, foreground.height, Bitmap.Config.ARGB_8888)
        val resizedBg = Bitmap.createScaledBitmap(
                background, foreground.width, foreground.height, false)
        val cv = Canvas(result)
        cv.drawBitmap(resizedBg, 0f, 0f, null)
        cv.drawBitmap(foreground, 0f, 0f, null)
        cv.save(Canvas.ALL_SAVE_FLAG)
        cv.restore()
        return result
    }

    private fun createImageFile(context: Context): File {
        val imageFileName = "JPEG_temp_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        )
        return image
    }

    fun getMapUrl(latLng: LatLng): String = "http://maps.google.com/maps?daddr=${latLng.latitude},${latLng.longitude}"

    fun getStreetSharingText(context: Context, latLng: LatLng): String {
        val mapText = getMapUrl(latLng)
        val stringBuilder = StringBuilder()
        stringBuilder.append(context.getString(R.string.sharing_pin_msg_description))
        stringBuilder.append(context.getString(R.string.sharing_map, mapText))
        stringBuilder.append("\n")

        return stringBuilder.toString()
    }

    fun getPinSharingText(context: Context, pin: PinDto): String {
        val mapText = getMapUrl(LatLng(pin.lat, pin.lon))
        val stringBuilder = StringBuilder()
        stringBuilder.append(context.getString(R.string.sharing_pin_msg_description))
        stringBuilder.append(context.getString(R.string.sharing_pin_title, pin.title))
        stringBuilder.append(context.getString(R.string.sharing_pin_description, pin.description))
        stringBuilder.append(context.getString(R.string.sharing_map, mapText))

        return stringBuilder.toString()
    }
}

