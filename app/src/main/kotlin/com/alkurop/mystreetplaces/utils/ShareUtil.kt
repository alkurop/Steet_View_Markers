package com.alkurop.mystreetplaces.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.provider.MediaStore
import com.alkurop.mystreetplaces.domain.pin.PinDto
import com.github.alkurop.streetviewmarker.CameraPosition
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.io.ByteArrayOutputStream
import com.squareup.picasso.Target as PicassoTarget

class ShareUtil {
    fun createShareIntentFromPin(pin: PinDto): Intent {
        val intent = Intent()
        return intent
    }

    fun createShareIntentFromStreetProjection(context: Context, cameraPosition: CameraPosition): Observable<Intent> {
        val subject = PublishSubject.create<Bitmap>()

        val target = object : PicassoTarget {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

            }

            override fun onBitmapFailed(errorDrawable: Drawable?) {
                subject.onError(IllegalStateException("Picasso could not load bitmap"))
            }

            override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom?) {
                subject.onNext(bitmap)
                subject.onComplete()
            }
        }

        val pathBuilder = StringBuilder()
        pathBuilder.append("https://maps.googleapis.com/maps/api/streetview?")
                .append("size=400x800")
                .append("&location=${cameraPosition.location.latitude},${cameraPosition.location.longitude}")
                .append("&fov=80")
                .append("&heading=${cameraPosition.camera.bearing}")
                .append("&pitch=${cameraPosition.camera.tilt}")
        Picasso.with(context).load(pathBuilder.toString()).into(target)

        return subject.map {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/jpeg"
            val bytes = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.JPEG, 80, bytes)
            val uri = MediaStore.Images.Media.insertImage(context.contentResolver, it, "StreetViewShareImage", null)
            intent.putExtra(Intent.EXTRA_STREAM, uri)
        }
    }
}