package com.alkurop.mystreetplaces.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.alkurop.mystreetplaces.domain.pin.PinDto
import com.github.alkurop.streetviewmarker.CameraPosition
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import com.squareup.picasso.Target as PicassoTarget
import android.graphics.Bitmap.CompressFormat
import android.R.attr.bitmap
import android.support.v4.content.FileProvider
import java.io.FileOutputStream

class ShareUtil {
    fun createShareIntentFromPin(pin: PinDto): Intent {
        val intent = Intent()
        return intent
    }

    fun createShareIntentFromStreetProjection(context: Context, cameraPosition: CameraPosition): Observable<Intent> {
        val observable = Observable.create<Bitmap> {
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
            Picasso.with(context).load(pathBuilder.toString()).into(target)
        }



        return observable.map { bitmap ->
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/*"
            val imageFile = createImageFile(context)

            val fOut = FileOutputStream(imageFile)
            bitmap.compress(CompressFormat.PNG, 100, fOut)
            fOut.flush()
            fOut.close()


            val photoURI = FileProvider.getUriForFile(context,
                    CameraPictureHelperImpl.FILE_PROVIDER,
                    imageFile)
            intent.putExtra(android.content.Intent.EXTRA_TEXT, "privet")

            intent.putExtra(Intent.EXTRA_STREAM, photoURI)
            intent
        }
    }


    private fun createImageFile(context: Context): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        )
        return image
    }
}