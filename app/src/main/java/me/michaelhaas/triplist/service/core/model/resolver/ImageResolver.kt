package me.michaelhaas.triplist.service.core.model.resolver

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.lang.IllegalArgumentException

sealed class ImageResolver : BaseResolver<Bitmap>() {

    class WebImageResolver(val url: Uri, private val callback: WebResolverCallback<Bitmap>?) : ImageResolver() {
        private var cached: Bitmap? = null

        override suspend fun resolve() =
            try {
                cached ?: Picasso.get().load(url).get()?.also {
                    cached = it
                    fireCallback(it)
                }
            } catch (e: IOException) {
                null
            }

        private suspend fun fireCallback(bmp: Bitmap) = coroutineScope {
            launch(Dispatchers.IO) {
                callback?.invoke(bmp)
            }
        }
    }

    class CacheImageResolver(val encodedImage: String) : ImageResolver() {
        private var cached: Bitmap? = null

        override suspend fun resolve(): Bitmap? =
            try {
                cached ?: Base64.decode(encodedImage, Base64.DEFAULT)?.let {
                    BitmapFactory.decodeByteArray(
                        it,
                        0,
                        it.size
                    )
                }?.also {
                    cached = it
                }
            } catch (e: IllegalArgumentException) {
                null
            }
    }
}