package com.github.bradmartin.cameraplus

import android.content.Context
import android.net.Uri
import android.support.media.ExifInterface
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

const val fTAG = "CameraPlus.FileSystem"

fun saveImageFile(context: Context, file: File, data: ByteArray, saveToGallery: Boolean?) {
    try {
        val fos = FileOutputStream(file)
        fos.write(data)
        fos.close()

        // if we are saving to gallery
        if (saveToGallery == true) {
            val exifInterface = ExifInterface(file.path)
            val orientation = exifInterface.getAttribute("Orientation")
            Log.i(fTAG, "orientation $orientation")
            val contentUri = Uri.fromFile(file)
            Log.i(fTAG, "contentUri $contentUri")
            val mediaScanIntent = android.content.Intent(
                    "android.intent.action.MEDIA_SCANNER_SCAN_FILE",
                    contentUri
            )

            Log.i(fTAG, "Sending broadcast for Intent $mediaScanIntent")
            // Broadcasts an intent with the new image, this tells the OS an image has been added so it will show in the gallery.
            context.sendBroadcast(mediaScanIntent)
        }
    } catch (error: IOException) {
        Log.e(fTAG, "Error saveImageToDisk $error")
        throw IOException("Error writing File to disk: $error")
    }
}
