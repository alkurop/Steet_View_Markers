package com.alkurop.mystreetplaces.utils

import android.content.Intent
import java.io.File

interface CameraPictureHelper {

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

    fun execute(listener: (File) -> Unit)
}