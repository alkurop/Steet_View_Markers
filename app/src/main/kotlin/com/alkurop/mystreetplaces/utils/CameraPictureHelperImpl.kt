package com.alkurop.mystreetplaces.utils

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CameraPictureHelperImpl : CameraPictureHelper {
    companion object {
        val FILE_PROVIDER = "com.alkurop.mystreetplaces.fileprovider"
    }
    private var requestCode:Int = 2212
    val fragment: Fragment?
    val activity: AppCompatActivity?
    lateinit var listener: (File) -> Unit
    lateinit var photoFile: File

    constructor(fragment: Fragment) {
        this.fragment = fragment
        this.activity = null
    }

    constructor(activity: AppCompatActivity) {
        this.fragment = null
        this.activity = activity
    }

    override fun execute(listener: (File) -> Unit) {
        this.listener = listener
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val act = activity ?: fragment!!.activity
        photoFile = createImageFile(act)
        val photoURI = FileProvider.getUriForFile(act,
                FILE_PROVIDER,
                photoFile)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)

        activity?.let {
            if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
                activity.startActivityForResult(takePictureIntent, requestCode)
            }
        }

        fragment?.let {
            if (takePictureIntent.resolveActivity(fragment.activity.packageManager) != null) {
                fragment.startActivityForResult(takePictureIntent, requestCode)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == this.requestCode && resultCode == RESULT_OK) {
            listener.invoke(photoFile)
            galleryAddPic(photoFile.absolutePath, activity ?: fragment!!.activity)
        }
    }

    private fun createImageFile(activity: Activity): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir      /* directory */
        )
        return image
    }

    private fun galleryAddPic(photoPath: String, activity: Activity) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val f = File(photoPath)
        val contentUri = Uri.fromFile(f)
        mediaScanIntent.data = contentUri
        activity.sendBroadcast(mediaScanIntent)
    }

    override fun setRequestCode(code: Int) {
        requestCode = code
    }
}

