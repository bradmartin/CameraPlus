package com.github.bradmartin.cameraplus

import android.hardware.camera2.CameraMetadata
import android.support.media.ExifInterface
import android.util.Log
import java.io.ByteArrayInputStream
import java.io.IOException

const val iTAG = "CameraPlus.Images"

/**
 * Returns the exif data from the camera byte array
 */
fun getOrientationFromBytes(data: ByteArray, cameraId: Int): Int {
    val inputStream = ByteArrayInputStream(data)
    val exif = android.support.media.ExifInterface(inputStream)
    var orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)

    try {
        inputStream.close()
    } catch (error: IOException) {
        Log.e(iTAG, "Error closing ByteArrayInputStream $error")
    }

    if (cameraId == CameraMetadata.LENS_FACING_BACK) {
        when (orientation) {
            ExifInterface.ORIENTATION_NORMAL -> orientation = ExifInterface.ORIENTATION_FLIP_HORIZONTAL
            ExifInterface.ORIENTATION_ROTATE_180 -> orientation = ExifInterface.ORIENTATION_FLIP_VERTICAL
            ExifInterface.ORIENTATION_ROTATE_90 -> orientation = ExifInterface.ORIENTATION_TRANSVERSE
        }
    }

    Log.i(iTAG, "Orientation $orientation")
    return orientation
}