package com.tincho5588.reddittopsreader.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.IOException
import java.io.OutputStream


object Utils {
    fun calculateCreatedTimeHours(created_utc_seconds: Long): Int {
        return ((System.currentTimeMillis() / 1000 - created_utc_seconds) / 3600).toInt()
    }

    fun downloadPictureToStorage(
        context: Context,
        imageName: String,
        bitmap: Bitmap,
        resultCallback: (result: Boolean) -> Unit
    ) {
        val contentResolver = context.contentResolver
        val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        var stream: OutputStream? = null
        var uri: Uri? = null

        try {
            val relativeLocation =
                Environment.DIRECTORY_PICTURES

            val contentValues = ContentValues().apply {
                put(MediaStore.Images.ImageColumns.DISPLAY_NAME, imageName)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.ImageColumns.RELATIVE_PATH, relativeLocation)
                }
            }

            uri = contentResolver.insert(contentUri, contentValues)
                ?: throw IOException("Failed to create new MediaStore record.")
            stream = contentResolver.openOutputStream(uri)

            if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)) {
                throw IOException("Failed to save bitmap.")
            }

            resultCallback(true)
        } catch (e: IOException) {
            uri ?: contentResolver.delete(uri!!, null, null)

            resultCallback(false)
        } finally {
            stream?.close()
        }
    }
}