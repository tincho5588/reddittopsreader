package com.tincho5588.reddittopsreader.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.tincho5588.reddittopsreader.R
import java.io.File
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
                Environment.DIRECTORY_PICTURES + File.pathSeparator + context.getString(R.string.app_name)

            val contentValues = ContentValues().apply {
                put(MediaStore.Images.ImageColumns.DISPLAY_NAME, imageName)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")

                // Scoped Storage: On Android P devices images will go to the root of the Pictures folder.
                // Having a different code path to adopt the legacy method only to achieve storing
                // the picture in a subfolder is not worth it.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.ImageColumns.RELATIVE_PATH, relativeLocation)
                }
            }

            uri = contentResolver.insert(contentUri, contentValues)?: throw IOException("Failed to create new MediaStore record.")
            stream = contentResolver.openOutputStream(uri)

            if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)) {
                throw IOException("Failed to save bitmap.")
            }

            resultCallback(true)
        } catch (e: IOException) {
            uri?:contentResolver.delete(uri!!, null, null)

            resultCallback(false)
        } finally {
            stream?.close()
        }
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCapabilities = connectivityManager.activeNetwork ?: return false

        val activeNetwork =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    fun showNetworkUnavailableToast(context: Context) {
        Toast.makeText(context, context.getString(R.string.network_unavailable_message), Toast.LENGTH_SHORT).show()
    }
}