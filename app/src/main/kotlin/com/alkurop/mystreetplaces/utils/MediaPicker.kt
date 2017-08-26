package com.alkurop.mystreetplaces.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.webkit.MimeTypeMap
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.io.*

private val CAMERA_URI = "pendingCamera"

/**
 * Created by dima on 12.05.16.
 */
class MediaPicker(val permissionsRequiredCallback: ((permission: String) -> Unit)) {
    val CODE_GALLERY = 1001
    val CODE_CAMERA = 1002
    var currentCode: Int? = null
    var pendingCameraUri: Uri? = null
    lateinit var context: Context

    fun fromGallery(activity: Activity, mediaType: MediaType) {
        context = activity
        if (!checkPermissions(activity)) return
        try {
            activity.startActivityForResult(getGalleryIntent(mediaType), CODE_GALLERY)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun fromCamera(activity: Activity, mediaType: MediaType) {
        context = activity
        if (!checkPermissions(activity)) return
        try {
            activity.startActivityForResult(getCameraIntent(mediaType), CODE_CAMERA)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun fromGallery(fragment: Fragment, mediaType: MediaType) {
        context = fragment.activity
        if (!checkPermissions(fragment.activity)) {
            currentCode = CODE_CAMERA
            return
        }
        try {
            currentCode = null
            fragment.startActivityForResult(getGalleryIntent(mediaType), CODE_GALLERY)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun fromCamera(fragment: Fragment, mediaType: MediaType) {
        context = fragment.activity
        if (!checkPermissions(fragment.activity)) {
            currentCode = CODE_CAMERA
            return
        }
        try {
            currentCode = null
            fragment.startActivityForResult(getCameraIntent(mediaType), CODE_CAMERA)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private fun getCameraIntent(mediaType: MediaType): Intent {
        val intent = Intent(if (mediaType == MediaType.PHOTO) MediaStore.ACTION_IMAGE_CAPTURE else MediaStore.ACTION_VIDEO_CAPTURE)
        val ext = if (mediaType == MediaType.PHOTO) ".jpg" else ".mp4"
        pendingCameraUri = Uri.fromFile(File(getFileDirectory(), createFileName(ext)))
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pendingCameraUri)
        return intent
    }

    private fun getGalleryIntent(mediaType: MediaType): Intent {
        val action: String = Intent.ACTION_GET_CONTENT
        val intent = Intent(action)
        intent.type = if (mediaType == MediaType.PHOTO) "image/*" else "video/*"
        return intent
    }

    private fun checkPermissions(context: Context): Boolean {
        val write = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val list = ArrayList<String>()
        if (write != PackageManager.PERMISSION_GRANTED) {
            list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (list.size > 0) {
            permissionsRequiredCallback.invoke(list[0])
        }
        return list.size == 0

    }

    fun handleResult(requestCode: Int, resultCode: Int, data: Intent?, callback: (Uri) -> Unit) {
        if (requestCode == CODE_GALLERY && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return
            }
            val contentUri = data.data
            if (contentUri !== null) {
                copyToLocal(contentUri)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ uri: Uri -> callback.invoke(uri) },
                                { Timber.e(it) })
            }
        } else if (requestCode == CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            if (pendingCameraUri != null) {
                callback.invoke(pendingCameraUri!!)
            }
        }
    }

    private fun copyToLocal(uri: Uri): Observable<Uri> {
        return Observable.just(uri)
                .flatMap { localUri: Uri ->
                    var bufferedOutputStream: BufferedOutputStream? = null
                    var bufferedInputStream: BufferedInputStream? = null
                    var file: File? = null
                    try {
                        val contentResolver = context.applicationContext.contentResolver
                        val type = contentResolver.getType(localUri)
                        val singleton = MimeTypeMap.getSingleton()
                        var ext = singleton.getExtensionFromMimeType(type)
                        ext = if (null === ext) {
                            ".jpg"
                        } else {
                            "." + ext
                        }
                        file = File(getFileDirectory(), createFileName(ext))
                        bufferedOutputStream = file.outputStream().buffered()
                        bufferedInputStream = contentResolver.openInputStream(localUri).buffered()
                        bufferedInputStream.copyTo(bufferedOutputStream)
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    } finally {
                        bufferedInputStream.safeClose()
                        bufferedOutputStream.safeClose()
                    }
                    if (file === null) Observable.error<Uri>(IOException("Failed to save file"))
                    Observable.just(Uri.fromFile(file))
                }


    }

    fun onSaveState(bundle: Bundle) {
        bundle.putParcelable(CAMERA_URI, pendingCameraUri)
    }

    fun onRestore(bundle: Bundle) {
        pendingCameraUri = bundle.getParcelable<Uri>(CAMERA_URI)
    }

    fun getFileDirectory(): String {

        val directory: File = File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "images");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directory.absolutePath
    }

    fun createFileName(ext: String): String = "" + System.currentTimeMillis() + ext

    fun clearFolder(file: File?) {
        if (file == null) return
        if (file.isDirectory) {
            val list = file.list();
            for (child in list) {
                File(child).delete()
            }
        }
    }

    fun InputStream?.safeClose() {
        try {
            this?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun OutputStream?.safeClose() {
        try {
            this?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

enum class MediaType {
    PHOTO,
    VIDEO
}