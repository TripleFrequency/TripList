package me.michaelhaas.triplist.service.core.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.IOException

sealed class Image {

    abstract suspend fun resolve(): Bitmap?

    data class WebImage(val url: Uri, private val postLoadCallback: (suspend (Bitmap?) -> Unit)? = null) : Image() {
        override suspend fun resolve() =
            try {
                Picasso.get().load(url).get()?.also {
                    fireCallback(it)
                }
            } catch (e: IOException) {
                null
            }

        private suspend fun fireCallback(bmp: Bitmap) = coroutineScope {
            launch(Dispatchers.IO) {
                postLoadCallback?.invoke(bmp)
            }
        }
    }

    data class CacheImage(val encodedImage: String) : Image() {
        override suspend fun resolve(): Bitmap? =
            Base64.decode(encodedImage, Base64.DEFAULT)?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
    }
}